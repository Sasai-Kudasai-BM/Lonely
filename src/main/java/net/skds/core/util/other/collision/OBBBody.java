package net.skds.core.util.other.collision;

import java.util.Iterator;
import java.util.stream.Stream;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.skds.core.util.mat.Vec3;
import net.skds.core.util.other.collision.OBBCollision.CollisionCallback;

public class OBBBody<E extends OBBBodyEntity> {
	public final E entity;
	public PhysData pd;

	public OBBBody(E entity) {
		this.entity = entity;
		this.pd = new PhysData();
	}

	public void tick() {
		pd.motion = new Vec3(entity.getMotion());
		AxisAlignedBB largeBox = entity.getBoundingBox();
		CollisionCallback cc = getCC(pd.motion, largeBox);

		if (cc.colide) {
			move(pd.motion.scale(cc.depth));
			onCollision(cc, pd.motion);
		} else {
			move(pd.motion);
		}

		setMotion();
	}

	private void onCollision(CollisionCallback cc, Vec3 motion) {
		Vec3 normal = cc.normal.dotProduct(motion) < 0 ? cc.normal : cc.normal.inverse();
		double normProjVel = motion.ProjOnNormalized(normal);
		Vec3 slideVel = motion.add(motion.scale(normProjVel));

		//System.out.println(cc.normal);

		pd.motion = slideVel;
	}

	private CollisionCallback getCC(Vec3 vec, AxisAlignedBB axisalignedbb) {
		CollisionCallback cc = CollisionCallback.NONE;
		if (vec.lengthSquared() == 0.0D) {
			return cc;
		}
		Stream<VoxelShape> stream2 = entity.world.getCollisionShapes(entity,
				axisalignedbb.expand(vec.getMojangD()).grow(1E-4));
		ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(stream2);

		Vec3 move = vec;
		OBBShape shape = entity.getShape();
		double depth = 1;
		Iterator<VoxelShape> iterator = reuseablestream.createStream().iterator();
		while (iterator.hasNext()) {
			VoxelShape vs = iterator.next();
			for (AxisAlignedBB aabb : vs.toBoundingBoxList()) {
				for (OBB boxB : shape.getBoxes()) {
					CollisionCallback c = OBBCollision.OBB2AABBColide(aabb,
							boxB.offset(new Vec3(entity.getPositionVec())), move);
					if (c.colide && depth > c.depth) {
						depth = c.depth;
						cc = c;
					}
				}
			}
		}
		return cc;
	}

	private void move(Vec3 move) {
		entity.setPosition(entity.getPosX() + move.x, entity.getPosY() + move.y, entity.getPosZ() + move.z);
	}

	private void setMotion() {
		entity.setMotion(pd.motion.getMojangD());
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
