package net.skds.core.util.other.collision;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.mat.Matrix3;
import net.skds.core.util.mat.Vec3;
import net.skds.core.util.other.collision.OBBCollision.Pair;

public class OBB {
	public final Vec3 center;
	public final Vec3[] points;
	public final Vec3[] normals;

	public final AxisAlignedBB aabb;

	public final String name;

	public Vec3[] debug = new Vec3[0];

	public OBB(Vec3[] points, Vec3[] normals, Vec3 center, String name) {
		this.center = center;
		this.normals = normals;
		this.points = points;
		this.name = name;
		this.aabb = getAABB();
	}

	public OBB(Vec3[] points, Vec3[] normals, Vec3 center) {
		this(points, normals, center, "unnamed");
	}

	public static OBB create(AxisAlignedBB aabb, Matrix3 matrix, Vec3 center) {
		return create(aabb, matrix, center, "unnamed");
	}

	public static OBB create(AxisAlignedBB aabb, Matrix3 matrix, Vec3 center, String name) {
		Vec3[] norms = new Vec3[6];
		norms[0] = Vec3.XP.transform(matrix);
		norms[1] = Vec3.YP.transform(matrix);
		norms[2] = Vec3.ZP.transform(matrix);
		norms[3] = norms[0].inverse();
		norms[4] = norms[1].inverse();
		norms[5] = norms[2].inverse();

		Vec3 c2 = center.transform(matrix).subtract(center);

		Vec3[] points = new Vec3[8];
		points[0] = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ).transform(matrix).subtract(c2);
		points[1] = new Vec3(aabb.maxX, aabb.maxY, aabb.minZ).transform(matrix).subtract(c2);
		points[2] = new Vec3(aabb.maxX, aabb.minY, aabb.maxZ).transform(matrix).subtract(c2);
		points[3] = new Vec3(aabb.maxX, aabb.minY, aabb.minZ).transform(matrix).subtract(c2);
		points[4] = new Vec3(aabb.minX, aabb.maxY, aabb.maxZ).transform(matrix).subtract(c2);
		points[5] = new Vec3(aabb.minX, aabb.maxY, aabb.minZ).transform(matrix).subtract(c2);
		points[6] = new Vec3(aabb.minX, aabb.minY, aabb.maxZ).transform(matrix).subtract(c2);
		points[7] = new Vec3(aabb.minX, aabb.minY, aabb.minZ).transform(matrix).subtract(c2);

		return new OBB(points, norms, center, name);
	}

	public static OBB create(AxisAlignedBB aabb) {

		Vec3[] points = new Vec3[8];
		points[0] = new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
		points[1] = new Vec3(aabb.maxX, aabb.maxY, aabb.minZ);
		points[2] = new Vec3(aabb.maxX, aabb.minY, aabb.maxZ);
		points[3] = new Vec3(aabb.maxX, aabb.minY, aabb.minZ);
		points[4] = new Vec3(aabb.minX, aabb.maxY, aabb.maxZ);
		points[5] = new Vec3(aabb.minX, aabb.maxY, aabb.minZ);
		points[6] = new Vec3(aabb.minX, aabb.minY, aabb.maxZ);
		points[7] = new Vec3(aabb.minX, aabb.minY, aabb.minZ);

		Vec3[] norms = new Vec3[6];
		norms[0] = Vec3.XP;
		norms[1] = Vec3.YP;
		norms[2] = Vec3.ZP;
		norms[3] = Vec3.XN;
		norms[4] = Vec3.YN;
		norms[5] = Vec3.ZN;

		return new OBB(points, norms, Vec3.ZERO);
	}

	public Vec3[] get3Normals() {
		return new Vec3[] { normals[0], normals[1], normals[2] };
	}

	public OBB offsetCenter(Vec3 offset) {
		return new OBB(points, normals, center.add(offset), name);
	}

	public OBB offset(Vec3 offset) {
		Vec3[] points = new Vec3[8];

		for (int i = 0; i < points.length; i++) {
			points[i] = this.points[i].add(offset);
		}

		return new OBB(points, normals, center, name);
	}

	public OBB rotate(Matrix3 matrix) {

		Vec3[] points = new Vec3[8];
		Vec3[] normals = new Vec3[6];

		Vec3 c2 = center.transform(matrix).subtract(center);

		for (int i = 0; i < points.length; i++) {
			points[i] = this.points[i].transform(matrix).subtract(c2);
		}

		normals[0] = this.normals[0].transform(matrix);
		normals[1] = this.normals[1].transform(matrix);
		normals[2] = this.normals[2].transform(matrix);
		normals[3] = normals[0].inverse();
		normals[4] = normals[1].inverse();
		normals[5] = normals[2].inverse();

		return new OBB(points, normals, center, name);
	}

	public Vec3 getCenter() {
		Vec3 sum = Vec3.ZERO;
		for (Vec3 point : points) {
			sum = sum.add(point);
		}
		return sum.scale(1D / 8);
		//return new Vec3(aabb.getCenter());
	}

	public Vec3 rayTrace(Vector3f from, Vector3f to) {
		return rayTrace(new Vec3(from), new Vec3(to));
	}

	public Vec3 rayTrace(Vector3d from, Vector3d to) {
		return rayTrace(new Vec3(from), new Vec3(to));
	}

	public Vec3 rayTrace(Vec3 from, Vec3 to) {

		debug = new Vec3[0];

		Vec3 dir = to.subtract(from);
		Vec3 delta = from.scale(-1);

		double tMax = Double.POSITIVE_INFINITY;
		double tMin = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < 3; i++) {
			double nomLen = normals[i].dotProduct(delta);
			double denomLen = normals[i].dotProduct(dir);

			if (Math.abs(denomLen) > 1E-7) {
				double a = (nomLen + ProjAxis(normals[i]).max) / denomLen;
				double b = (nomLen + ProjAxis(normals[i]).min) / denomLen;

				double min = Math.min(a, b);
				double max = Math.max(a, b);

				if (min > tMin) {
					tMin = min;
				}
				if (max < tMax) {
					tMax = max;
				}

				if (tMax < tMin) {
					return null;
				}

			}
		}
		//System.out.println(name + " " + normals[1]);
		debug = ArrayUtils.addAll(debug, new Vec3[] { from.add(dir.scale(tMax)), from.add(dir.scale(tMin)) });

		return from.add(dir.scale(tMin));
	}

	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, float r, float g, float b, float a) {

		Matrix4f m4f = matrixStack.getLast().getMatrix();
		for (int k = 0; k < 3; k++) {
			for (int i = 0; i < 8; i++) {
				int n = i << k;
				int j = n % 8;
				j += n >> 3;
				Vec3 vertex = points[j % 8];
				buffer.pos(m4f, (float) vertex.x, (float) vertex.y, (float) vertex.z).color(r, g, b, a).endVertex();
			}
		}
		for (Vec3 deb : debug) {
			buffer.pos(m4f, (float) deb.x, (float) deb.y, (float) deb.z).color(0F, 0F, 1F, 1F).endVertex();
		}
		for (Vec3 deb : debug) {
			//System.out.println(deb);
			double d = 0.01;
			AxisAlignedBB smallaabb = new AxisAlignedBB(d, d, d, -d, -d, -d);
			smallaabb = smallaabb.offset(deb.getMojangD());
			WorldRenderer.drawBoundingBox(matrixStack, buffer, smallaabb, 1, 0, 0, 1);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public void renderVBO(MatrixStack matrixStack, IVertexBuilder buffer, float r, float g, float b, float a) {

		Matrix4f m4f = matrixStack.getLast().getMatrix();
		//System.out.println("x");
		int[] cof = {2, 3, 1, 0, 4, 5, 7, 6, 0, 1, 5, 4, 6, 7, 3, 2, 4, 6, 2, 0, 1, 3, 7, 5};

		//int[] cof = {2, 3, 1, 0, 4, 5, 7, 6};
		//int[] cof = {0, 1, 5, 4, 6, 7, 3, 2};
		//int[] cof = {4, 6, 2, 0, 1, 3, 7, 5};
		for (int j : cof) {
			Vec3 vertex = points[j % 8];
			buffer.pos(m4f, (float) vertex.x, (float) vertex.y, (float) vertex.z).color(r, g, b, a).endVertex();
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

	public Pair ProjAxis(Vec3 direct) {
		Pair pair = new Pair();
		pair.max = Double.NEGATIVE_INFINITY;
		pair.min = Double.POSITIVE_INFINITY;
		for (Vec3 point : points) {
			double proj = point.ProjOnNormalized(direct);
			if (proj > pair.max) {
				pair.max = proj;
			}
			if (proj < pair.min) {
				pair.min = proj;
			}
		}
		return pair;
	}
}