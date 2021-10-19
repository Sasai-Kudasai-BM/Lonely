package net.skds.core.util.other.collision;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.util.math.AxisAlignedBB;
import net.skds.core.util.mat.Vec3;

public class OBBCollision {

	
	private static final Vec3[] NORMALS = { Vec3.XP, Vec3.YP, Vec3.ZP };

	public static CollisionCallback OBB2AABBColide(AxisAlignedBB aabb, OBB obb, Vec3 velocityBA) {
		Vec3[] terminators = ArrayUtils.addAll(obb.get3Normals(), NORMALS);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Vec3 cross = obb.normals[i].crossProduct(NORMALS[j]);
				if (cross.lengthSquared() > 1E-9) {
					terminators = ArrayUtils.addAll(terminators, cross.normalize());
				}
			}
		}
		return IntersectionOfProj(OBB.create(aabb), obb, terminators, velocityBA);
	}

	public static CollisionCallback OBB2OBBColide(OBB obb1, OBB obb2, Vec3 velocityBA) {
		Vec3[] terminators = ArrayUtils.addAll(obb1.get3Normals(), obb2.get3Normals());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Vec3 cross = obb1.normals[i].crossProduct(obb2.normals[j]);
				if (cross.lengthSquared() > 1E-9) {
					terminators = ArrayUtils.addAll(terminators, cross.normalize());
				}
			}
		}
		return IntersectionOfProj(obb1, obb2, terminators, velocityBA);
	}

	private static CollisionCallback IntersectionOfProj(OBB a, OBB b, Vec3[] terminators, Vec3 velocityBA) {

		ProjPair projInterval = new ProjPair(0, 1);
		Vec3 minTeminator = Vec3.YP;
		double minL = Double.MAX_VALUE;

		for (Vec3 terminator : terminators) {
			double v = velocityBA.ProjOnNormalized(terminator);
			double v2 = Math.abs(v);
			ProjPair projA = a.ProjAxis(terminator);
			ProjPair projB = b.ProjAxis(terminator);
			if (v2 < 1E-7) {
				projA.intersect(projB);
				if (!projA.isNormal()) {
					projInterval.max = -1;
					projInterval.min = 1;
					break;
				}
				continue;
			}
			double zero;
			double inner;
			double outer;
			if (v > 0) {
				zero = projB.min;
				outer = projA.max;
				inner = projA.min - projB.len();
			} else {
				zero = projB.max;
				outer = projA.min;
				inner = projA.max + projB.len();
			}
			inner -= zero;
			outer -= zero;
			ProjPair inter = new ProjPair(inner / v, outer / v);
			if (inter.len() < minL) {
				minL = inter.len();
				minTeminator = terminator;
				minL = projA.len();
			}
			projInterval.intersect(inter);
			if (!projInterval.isNormal()) {
				break;
			}
		}
		boolean intersect = projInterval.isNormal();
		Vec3 point = Vec3.ZERO;
		if (intersect) {
			for (Vec3 normal : NORMALS) {
				ProjPair proj = a.ProjAxis(normal).intersect(b.ProjAxis(normal).move(velocityBA.ProjOnNormalized(normal) * projInterval.min));
				point = point.add(normal.scale(proj.mid()));
			}
		}
		return new CollisionCallback(intersect, projInterval.min, minTeminator, point);
	}

	public static class CollisionCallback {

		public static final CollisionCallback NONE = new CollisionCallback(false, 1.0, Vec3.YP, Vec3.ZERO);

		public final boolean colide;
		public final double depth;
		public final Vec3 normal;
		public final Vec3 hitPoint;

		public CollisionCallback(boolean colide, double depth, Vec3 normal, Vec3 hitPoint) {
			this.colide = colide;
			this.depth = depth;
			this.normal = normal;
			this.hitPoint = hitPoint;
		}
	}

	public static class ProjPair {
		public double max = 0;
		public double min = 0;

		public ProjPair(double min, double max) {
			this.min = min;
			this.max = max;
		}

		public static ProjPair projAABB(AxisAlignedBB aabb, Vec3 axis) {
			OBB obb = OBB.create(aabb);
			return obb.ProjAxis(axis);
		}

		public ProjPair() {
		}

		public double len() {
			return max - min;
		}

		public double mid() {
			return (max + min) / 2;
		}

		public boolean isNormal() {
			return len() > 0;
		}

		public ProjPair intersect(ProjPair proj) {
			this.min = Math.max(proj.min, this.min);
			this.max = Math.min(proj.max, this.max);
			return this;
		}

		public ProjPair union(ProjPair proj) {
			this.min = Math.min(proj.min, this.min);
			this.max = Math.max(proj.max, this.max);
			return this;
		}

		public ProjPair move(double d) {
			min += d;
			max += d;
			return this;
		}

		public ProjPair copy() {
			return new ProjPair(min, max);
		}
	}
}