package net.skds.core.util.other.collision;

import java.util.Optional;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.mat.Matrix3;
import net.skds.core.util.mat.Vec3;

public class OBB {
	public final Vec3 center;
	public final Vec3[] points;
	public final Vec3[] normals;

	public final AxisAlignedBB aabbInitial;
	public final AxisAlignedBB aabb;

	public final Matrix3 matrix;

	public OBB(AxisAlignedBB aabb, Matrix3 matrix, Vec3 center) {
		this.aabbInitial = aabb;
		this.matrix = matrix.copy();
		Vec3[] norms = new Vec3[6];
		norms[0] = new Vec3(1, 0, 0).transform(matrix);
		norms[1] = new Vec3(0, 1, 0).transform(matrix);
		norms[2] = new Vec3(0, 0, 1).transform(matrix);
		norms[3] = norms[0].inverse();
		norms[4] = norms[1].inverse();
		norms[5] = norms[2].inverse();
		normals = norms;

		Vec3 aabbC = new Vec3(aabb.getCenter());
		this.center = center;

		Vec3[] points = new Vec3[8];
		points[0] = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ).subtract(aabbC).transform(matrix).add(center);
		points[1] = new Vec3(aabb.maxX, aabb.maxY, aabb.minZ).subtract(aabbC).transform(matrix).add(center);
		points[2] = new Vec3(aabb.maxX, aabb.minY, aabb.maxZ).subtract(aabbC).transform(matrix).add(center);
		points[3] = new Vec3(aabb.maxX, aabb.minY, aabb.minZ).subtract(aabbC).transform(matrix).add(center);
		points[4] = new Vec3(aabb.minX, aabb.maxY, aabb.maxZ).subtract(aabbC).transform(matrix).add(center);
		points[5] = new Vec3(aabb.minX, aabb.maxY, aabb.minZ).subtract(aabbC).transform(matrix).add(center);
		points[6] = new Vec3(aabb.minX, aabb.minY, aabb.maxZ).subtract(aabbC).transform(matrix).add(center);
		points[7] = new Vec3(aabb.minX, aabb.minY, aabb.minZ).subtract(aabbC).transform(matrix).add(center);
		this.points = points;

		this.aabb = getAABB();
	}

	public OBB(AxisAlignedBB aabb) {
		this.aabbInitial = aabb;
		this.matrix = new Matrix3();
		this.center = new Vec3(aabb.getCenter());
		
		Vec3[] points = new Vec3[8];
		points[0] = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
		points[1] = new Vec3(aabb.maxX, aabb.maxY, aabb.minZ);
		points[2] = new Vec3(aabb.maxX, aabb.minY, aabb.maxZ);
		points[3] = new Vec3(aabb.maxX, aabb.minY, aabb.minZ);
		points[4] = new Vec3(aabb.minX, aabb.maxY, aabb.maxZ);
		points[5] = new Vec3(aabb.minX, aabb.maxY, aabb.minZ);
		points[6] = new Vec3(aabb.minX, aabb.minY, aabb.maxZ);
		points[7] = new Vec3(aabb.minX, aabb.minY, aabb.minZ);
		this.points = points;

		Vec3[] norms = new Vec3[6];
		norms[0] = Vec3.XP;
		norms[1] = Vec3.YP;
		norms[2] = Vec3.ZP;
		norms[3] = Vec3.XN;
		norms[4] = Vec3.YN;
		norms[5] = Vec3.ZN;
		normals = norms;
		this.aabb = aabb;
	}

	public Optional<Vector3d> rayTrace(Vector3f from, Vector3f to) {
		return rayTrace(new Vec3(from), new Vec3(to));
	}

	public Optional<Vector3d> rayTrace(Vector3d from, Vector3d to) {
		return rayTrace(new Vec3(from), new Vec3(to));
	}

	public Optional<Vector3d> rayTrace(Vec3 from, Vec3 to) {
		Matrix3 matrix3 = matrix.copy();
		matrix3.transpose();
		return aabbInitial.rayTrace(from.subtract(center).transform(matrix3).getMojangD(), to.subtract(center).transform(matrix3).getMojangD());
	}

	@OnlyIn(Dist.CLIENT)
	public void render(Matrix4f m4f, IVertexBuilder buffer, float r, float g, float b, float a) {		
		for (int k = 0; k < 3; k++) {
			for (int i = 0; i < 8; i++) {
				int n = i << k;
				int j = n % 8;
				j += n >> 3;
				Vec3 vertex = points[j % 8];
				buffer.pos(m4f, (float) vertex.x, (float) vertex.y, (float) vertex.z).color(r, g, b, a).endVertex();
			}
		}
	}

	private AxisAlignedBB getAABB() {
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		double minZ = Double.POSITIVE_INFINITY;
		double maxZ = Double.NEGATIVE_INFINITY;

		for (Vec3 point : points) {
			minX = Math.min(minX, point.x);
			minY = Math.min(minY, point.y);
			minZ = Math.min(minZ, point.z);
			maxX = Math.max(maxX, point.x);
			maxY = Math.max(maxY, point.y);
			maxZ = Math.max(maxZ, point.z);
		}

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
}