package net.skds.lonely.client.inventory;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.BBParser;
import net.skds.core.util.other.collision.OBB;

@OnlyIn(Dist.CLIENT)
public class BodyPart {

	public final Segment segment;

	public final PlayerModel<AbstractClientPlayerEntity> model;

	public BodyPart(Segment segment, PlayerModel<AbstractClientPlayerEntity> model) {
		this.model = model;
		this.segment = segment;
	}

	public static BodyPart create(Segment segment, PlayerModel<AbstractClientPlayerEntity> model, List<BodyPart> map) {
		BodyPart part = new BodyPart(segment, model);
		map.add(part);
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
		matrixStack.translate(part.rotationPointX / -16.0F, part.rotationPointY / -16.0F, part.rotationPointZ / -16.0F);
	}

	public static List<ClickOBBShape> getPlayerShapes(AbstractClientPlayerEntity player) {
		List<ClickOBBShape> list = new ArrayList<>();
		for (OBB obb : BBParser.get("lonely/player_a")) {
			//System.out.println(obb.name);
			List<OBB> boxlist = new ArrayList<>();
			switch (obb.name) {
				case "head":
					boxlist.add(obb);
					list.add(new ClickOBBShape(boxlist, null, ItemStack.EMPTY, Segment.HEAD));
					break;
				case "body":
					boxlist.add(obb);
					list.add(new ClickOBBShape(boxlist, null, ItemStack.EMPTY, Segment.BODY));
					break;
				case "r_arm":
					boxlist.add(obb);
					list.add(new ClickOBBShape(boxlist, null, ItemStack.EMPTY, Segment.RIGHT_ARM));
					break;
				case "l_arm":
					boxlist.add(obb);
					list.add(new ClickOBBShape(boxlist, null, ItemStack.EMPTY, Segment.LEFT_ARM));
					break;
				case "r_leg":
					boxlist.add(obb);
					list.add(new ClickOBBShape(boxlist, null, ItemStack.EMPTY, Segment.RIGHT_LEG));
					break;
				case "l_leg":
					boxlist.add(obb);
					list.add(new ClickOBBShape(boxlist, null, ItemStack.EMPTY, Segment.LEFT_LEG));
					break;

				default:
					break;
			}
		}

		return list;
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
