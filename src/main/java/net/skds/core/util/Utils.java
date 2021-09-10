package net.skds.core.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class Utils {

	public static RaycastResult raycastEyes(float distance, boolean addEntity, Entity self, float extend) {
		return raycast(distance, self.world, BlockMode.COLLIDER, FluidMode.NONE, addEntity, self, self.getEyePosition(1), e -> true, extend);
	}
	public static RaycastResult raycast(float distance, World world, boolean addEntity, Entity self, Vector3d from) {
		return raycast(distance, world, BlockMode.COLLIDER, FluidMode.NONE, addEntity, self, from, e -> true, 0);
	}
	public static RaycastResult raycast(float distance, World world, boolean addEntity, Entity self, Vector3d from, Predicate<Entity> predicate, float extend) {
		return raycast(distance, world, BlockMode.COLLIDER, FluidMode.NONE, addEntity, self, from, predicate, extend);
	}
	public static RaycastResult raycast(float distance, World world, BlockMode blockMode, FluidMode fluidMode,
			boolean addEntity, Entity self, Vector3d from, Predicate<Entity> predicate, float extend) {

		float beamL = distance;
		float beamDist = beamL;
		Vector3d rot = self.getLookVec();
		Vector3d pos0 = from;
		Vector3d raySegFrom = pos0;
		boolean cross = false;
		Entity lastEntity = null;
		Vector3d point = pos0;
		BlockPos endPos = null;

		while (beamL > 0) {
			float len = Math.min(beamL, 10);
			beamL -= len;
			Vector3d dl = rot.scale(len);
			Vector3d raySegTo = raySegFrom.add(dl);
			BlockRayTraceResult brtr = world.rayTraceBlocks(
					new RayTraceContext(raySegFrom, raySegTo, blockMode, fluidMode, self));

			endPos = brtr.getPos();
			if (!brtr.getHitVec().equals(raySegTo)) {
				cross = true;
				beamDist = (float) Math.min(pos0.subtract(brtr.getHitVec()).length(), beamDist);
			}

			if (addEntity) {
				AxisAlignedBB box = AxisAlignedBB.fromVector(raySegFrom).expand(dl).grow(extend);
				List<Entity> list = self.world.getLoadedEntitiesWithinAABB(Entity.class, box, predicate);
				for (Entity e : list) {
					Optional<Vector3d> op = e.getBoundingBox().grow(extend).rayTrace(raySegFrom, raySegTo);
					if (op.isPresent()) {
						cross = true;
						Vector3d rt = op.get();
						float l = (float) pos0.subtract(rt).length();
						if (l < beamDist) {
							beamDist = l;
							lastEntity = e;
						}
					}
				}
			}
			point = pos0.add(rot.scale(beamDist));
			if (cross) {
				break;
			}
			raySegFrom = raySegTo;
		}
		return new RaycastResult(lastEntity, endPos, point, beamDist, cross);
	}

	public static class RaycastResult {
		public final BlockPos endBlockPos;
		public final Entity targetEntity;
		public final Vector3d endPos;
		public final float len;
		public final boolean cross;

		public RaycastResult(Entity targetEntity, BlockPos endBlockPos, Vector3d endPos, float len, boolean cross) {
			this.endBlockPos = endBlockPos;
			this.targetEntity = targetEntity;
			this.endPos = endPos;
			this.len = len;
			this.cross = cross;
		}
	}
}
