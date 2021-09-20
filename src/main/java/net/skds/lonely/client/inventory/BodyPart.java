package net.skds.lonely.client.inventory;

import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BodyPart {

	public final Segment segment;

	public final PlayerModel<AbstractClientPlayerEntity> model;

	public BodyPart(Segment segment, PlayerModel<AbstractClientPlayerEntity> model) {
		this.model = model;
		this.segment = segment;
	}

	public static BodyPart create(Segment segment, PlayerModel<AbstractClientPlayerEntity> model, Map<Segment, BodyPart> map) {
		BodyPart part = new BodyPart(segment, model);
		map.put(segment, part);
		return part;
	}


	private ModelRenderer getPart() {
		switch (segment) {
			case BODY:
				return model.bipedBody;
			case HEAD:
				return model.bipedHead;
			case LEFT_ARM:
				return model.bipedLeftArm;
			case RIGHT_ARM:
				return model.bipedRightArm;
			case LEFT_LEG:
				return model.bipedLeftLeg;
			case RIGHT_LEG:
				return model.bipedRightLeg;
		
			default:
				return model.bipedBody;
		}
	}

	public void applyRotationAndPos(MatrixStack matrixStack) {
		ModelRenderer part = getPart();
		
		matrixStack.translate(part.rotationPointX / 16.0F, part.rotationPointY / 16.0F, part.rotationPointZ / 16.0F);
		Quaternion q = new Quaternion(Vector3f.ZP, part.rotateAngleZ, false);
		q.multiply(new Quaternion(Vector3f.YP, part.rotateAngleY, false));
		q.multiply(new Quaternion(Vector3f.XP, part.rotateAngleX, false));
		matrixStack.rotate(q);
		matrixStack.translate(0.0D, 1.501F, 0.0D);
		float f = 1.066666F;
		matrixStack.scale(-f, -f, f);

	}
	public void reverseRotationAndPos(MatrixStack matrixStack) {
		ModelRenderer part = getPart();
		
		Quaternion q = new Quaternion(Vector3f.ZP, part.rotateAngleZ, false);
		q.multiply(new Quaternion(Vector3f.YP, part.rotateAngleY, false));
		q.multiply(new Quaternion(Vector3f.XP, part.rotateAngleX, false));
		float f = 1F / 1.066666F;
		matrixStack.scale(f, f, f);
		matrixStack.translate(0.0D - (part.rotationPointX / 16.0F), 1.501F - (part.rotationPointY / 16.0F), 0.0D - (part.rotationPointZ / 16.0F));
		q.conjugate();
		matrixStack.rotate(q);
		matrixStack.translate(0.0D, -1.501F, 0.0D);

		//System.out.println(part.rotationPointX + " " + segment.toString());

	}
	
	
	public static enum Segment {
		HEAD,
		BODY,
		LEFT_ARM,
		RIGHT_ARM,
		LEFT_LEG,
		RIGHT_LEG;
	}
}
