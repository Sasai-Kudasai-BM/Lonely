package net.skds.core.util.other.collision;

import java.util.stream.Stream;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.skds.core.util.mat.Vec3;

public class OBBBody<E extends OBBBodyEntity> {
	public final E entity;
	public PhysData physData;

	public OBBBody(E entity) {
		this.entity = entity;
		this.physData = new PhysData();
		//this.physData.motion = new Vec3(entity.getMotion());
	}

	public void tick() {
		//Vec3 motion = physData.motion;
		Vec3 motion = new Vec3(entity.getMotion());
		AxisAlignedBB largeBox = entity.getBoundingBox();

		//for (OBB obb : entity.getShape().boxes) {
		//	largeBox = largeBox.union(obb.aabb);
		//}

		Vec3 maxMove = getAllowedMovementV3(motion.getMojangD(), largeBox);

		if (maxMove.equals(motion)) {
			move(motion);
		} else {
		}

		physData.motion = motion;
		setMotion();
	}

	private void setMotion() {
		entity.setMotion(physData.motion.getMojangD());
	}

	private Vec3 getAllowedMovementV3(Vector3d vec, AxisAlignedBB axisalignedbb) {

		VoxelShape voxelshape = entity.world.getWorldBorder().getShape();
		Stream<VoxelShape> stream = VoxelShapes.compare(voxelshape, VoxelShapes.create(axisalignedbb),
				IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);

		ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(stream);

		Vector3d vector3d = vec.lengthSquared() == 0.0D ? vec
				: collideCustom(vec, axisalignedbb, entity.world, reuseablestream);

		return new Vec3(vector3d);
	}

	private Vector3d collideCustom(Vector3d vec, AxisAlignedBB collisionBox, World world,
			ReuseableStream<VoxelShape> potentialHits) {
		Stream<VoxelShape> stream = world.getCollisionShapes(entity, collisionBox.expand(vec));

		ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(
				Stream.concat(potentialHits.createStream(), stream));

		return Entity.collideBoundingBox(vec, collisionBox, reuseablestream);
	}

	private void move(Vec3 move) {
		entity.setPosition(entity.getPosX() + move.x, entity.getPosY() + move.y, entity.getPosZ() + move.z);
	}

	public static double approxAngle(double d) {
		double out = d % 360.0;
		if (out > 180.0D) {
			out -= 360.0;
		} else if (out < -180.0D) {
			out += 360.0;
		}
		return out;
	}

	public static class PhysData {

		public Vec3 motion = Vec3.ZERO;
		public Vec3 momentum = Vec3.SINGLE;
		public Vec3 spin = Vec3.ZERO;
		public Vec3 rotation = Vec3.ZERO;
		public Vec3 com = Vec3.ZERO;
		public double mass = 1D;

		protected void readAdditional(CompoundNBT compound) {
			if (compound.contains("Mass"))
				mass = compound.getDouble("Mass");

			ListNBT listnbt = compound.getList("Rotation3d", 6);
			rotation = new Vec3(OBBBody.approxAngle(listnbt.getDouble(0)), OBBBody.approxAngle(listnbt.getDouble(1)),
					OBBBody.approxAngle(listnbt.getDouble(2)));

			listnbt = compound.getList("Spin", 6);
			spin = new Vec3(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));

			listnbt = compound.getList("Momentum", 6);
			momentum = new Vec3(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));

			listnbt = compound.getList("COM", 6);
			com = new Vec3(listnbt.getDouble(0), listnbt.getDouble(1), listnbt.getDouble(2));

		}

		protected void writeAdditional(CompoundNBT compound) {
			compound.put("Rotation3d", this.newDoubleNBTList(rotation.x, rotation.y, rotation.z));
			compound.put("Spin", this.newDoubleNBTList(spin.x, spin.y, spin.z));
			compound.put("Momentum", this.newDoubleNBTList(momentum.x, momentum.y, momentum.z));
			compound.put("COM", this.newDoubleNBTList(com.x, com.y, com.z));
			compound.putDouble("Mass", mass);
		}

		private ListNBT newDoubleNBTList(double... numbers) {
			ListNBT listnbt = new ListNBT();

			for (double d0 : numbers) {
				listnbt.add(DoubleNBT.valueOf(d0));
			}

			return listnbt;
		}

	}

	public static class PhysDataSerializer implements IDataSerializer<PhysData> {

		public static void writeVec3(PacketBuffer buffer, Vec3 vec) {
			buffer.writeDouble(vec.x);
			buffer.writeDouble(vec.y);
			buffer.writeDouble(vec.z);
		}

		public static Vec3 readVec3(PacketBuffer buffer) {
			return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		}

		@Override
		public void write(PacketBuffer buf, PhysData value) {
			writeVec3(buf, value.com);
			writeVec3(buf, value.momentum);
			writeVec3(buf, value.motion);
			writeVec3(buf, value.rotation);
			writeVec3(buf, value.spin);
			buf.writeDouble(value.mass);
		}

		@Override
		public PhysData read(PacketBuffer buf) {
			PhysData newValue = new PhysData();

			newValue.com = readVec3(buf);
			newValue.momentum = readVec3(buf);
			newValue.motion = readVec3(buf);
			newValue.rotation = readVec3(buf);
			newValue.spin = readVec3(buf);
			newValue.mass = buf.readDouble();

			return newValue;
		}

		@Override
		public PhysData copyValue(PhysData value) {
			PhysData newValue = new PhysData();
			newValue.com = value.com;
			newValue.mass = value.mass;
			newValue.momentum = value.momentum;
			newValue.motion = value.motion;
			newValue.rotation = value.rotation;
			newValue.spin = value.spin;
			return newValue;
		}
	}
}
