package net.skds.core.util.other.collision;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.util.math.AxisAlignedBB;
import net.skds.core.util.mat.Vec3;

public class OBBCollision {
	
	public static Vec3 OBB2AABBColide(OBB obb, AxisAlignedBB aabb) {
		return OBB2OBBColide(obb, OBB.create(aabb));
	}

	public static Vec3 OBB2OBBColide(OBB obb1, OBB obb2) {
		Vec3[] terminators = ArrayUtils.addAll(obb1.get3Normals(), obb2.get3Normals());

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Vec3 cross = obb1.normals[i].crossProduct(obb2.normals[j]);
				if (cross.lengthSquared() > 1E-9) {
					terminators = ArrayUtils.addAll(terminators, cross);
				}
			}
		}

		return IntersectionOfProj(obb1, obb2, terminators);
	}

	private static Vec3 IntersectionOfProj(OBB a, OBB b, Vec3[] terminators) {

		Vec3 norm = new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

		for (Vec3 terminator : terminators) {
			Pair projA = a.ProjAxis(terminator);
			Pair projB = b.ProjAxis(terminator);

			double[] points = {projA.min, projA.max, projB.min, projB.max};
			Arrays.sort(points);

			double sum = (projA.max - projA.min) + (projB.max - projB.min);
			double len = Math.abs(points[3] - points[0]);

			if (sum <= len) {
				return Vec3.ZERO;
			}

			double dl = Math.abs(points[2] - points[1]);
			if (dl < norm.length()) {
				norm = terminator.scale(dl);
			}
		}
		return norm;
	}

	public static class Pair {
		public double max = 0;
		public double min = 0;

		public double len() {
			return max - min;
		}
	}
}