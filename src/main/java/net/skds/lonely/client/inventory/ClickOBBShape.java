package net.skds.lonely.client.inventory;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.mat.Vec3;
import net.skds.core.util.other.Pair;
import net.skds.core.util.other.collision.OBB;
import net.skds.core.util.other.collision.OBBShape;
import net.skds.lonely.client.inventory.BodyPart.Segment;
import net.skds.lonely.client.render.renderers.EquipmentLayerRenderer;
import net.skds.lonely.inventory.EquipmentLayer;

@OnlyIn(Dist.CLIENT)
public class ClickOBBShape extends OBBShape {

	public final EquipmentLayer layer;
	public final ItemStack stack;
	public final Segment segment;

	public ClickOBBShape(List<OBB> boxes, EquipmentLayer layer, ItemStack stack, Segment segment) {
		super(boxes);
		this.segment = segment;
		this.stack = stack;
		this.layer = layer;
	}
	
	public double hower(MatrixStack matrixStack, float x0, float y0) {
		double min = Double.POSITIVE_INFINITY;
		if (isEmpty()) {
			return min;
		}
		matrixStack.push();		
		float f = 1F / 0.9375F;
		matrixStack.scale(f, f, f);
		Pair<Matrix3f, Matrix4f> pair = EquipmentLayerRenderer.getTransform(segment);
		Matrix3f matrix3f = pair.a.copy();
		matrix3f.transpose();
		Matrix4f matrix4f = pair.b;
		TransformationMatrix transformationMatrix = new TransformationMatrix(matrix4f);
		Vector3f trans = transformationMatrix.getTranslation();
		Vector3f scale = transformationMatrix.getScale();
		float s = 1F / 60;
		trans.mul(-s);
		float sx = 1F / (scale.getX() * s);
		float sy = 1F / (scale.getY() * s);
		float sz = 1F / (scale.getZ() * s);
		Vector3f vf1 = new Vector3f(x0, y0, -100);
		Vector3f vf2 = new Vector3f(x0, y0, 100);
		vf1.add(trans);
		vf2.add(trans);
		vf1.transform(matrix3f);
		vf2.transform(matrix3f);
		vf1.mul(sx, sy, sz);
		vf2.mul(sx, sy, sz);
		matrixStack.pop();

		for (OBB obb : boxes) {
			Vec3 cross = obb.rayTrace(vf1, vf2);
			if (cross != null) {
				double len = cross.length();
				if (len < min) {
					min = len;
				}
			}
		}

		return min;
	}
}
