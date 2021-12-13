package net.skds.lonely.client.render.renderers;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.skds.core.util.other.Pair;
import net.skds.lonely.client.inventory.BodyPart;
import net.skds.lonely.inventory.EPlayerInventory;
import net.skds.lonely.inventory.EquipmentLayer;
import net.skds.lonely.item.ILonelyItem;

public class EquipmentLayerRenderer<T extends PlayerEntity, M extends PlayerModel<T> & IHasArm>
		extends LayerRenderer<T, M> {

	@SuppressWarnings("unused")
	private static final Minecraft mc = Minecraft.getInstance();
	private final EPlayerRenderer playerRenderer;

	public static final int size = EquipmentLayer.values().length;
	public static boolean inGui = false;

	private static final Map<BodyPart.Segment, Pair<Matrix3f, Matrix4f>> transformation = new HashMap<>();

	public static void setTransform(BodyPart.Segment segment, MatrixStack.Entry entry) {
		Quaternion q = new Quaternion(Vector3f.XP, 180, true);
		Matrix3f matrix3f = entry.getNormal().copy();
		Matrix4f matrix4f = entry.getMatrix().copy();
		matrix3f.mul(q);
		matrix4f.mul(q);
		transformation.put(segment, new Pair<Matrix3f, Matrix4f>(matrix3f, matrix4f));
	}

	public static Pair<Matrix3f, Matrix4f> getTransform(BodyPart.Segment segment) {
		Pair<Matrix3f, Matrix4f> pair = transformation.get(segment);
		if (pair == null) {
			Matrix3f matrix3f = new Matrix3f();
			matrix3f.setIdentity();
			Matrix4f matrix4f = new Matrix4f();
			matrix4f.setIdentity();
			pair = new Pair<Matrix3f,Matrix4f>(matrix3f, matrix4f);
		}
		return pair;
	}

	public static MatrixStack transform(BodyPart.Segment segment, MatrixStack matrixStack) {
		Pair<Matrix3f, Matrix4f> pair = transformation.get(segment);
		if (pair != null) {
			MatrixStack.Entry ms2Entry = matrixStack.getLast();
			ms2Entry.getNormal().mul(pair.a);
			ms2Entry.getMatrix().mul(pair.b);
		}
		return matrixStack;
	}

	public EquipmentLayerRenderer(IEntityRenderer<T, M> playerRenderer) {
		super(playerRenderer);
		int ddddd;
		// TODO
		//this.playerRenderer = (EPlayerRenderer) playerRenderer;
		this.playerRenderer = null;
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T player, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		EPlayerInventory inventory = (EPlayerInventory) player.inventory;
		if (!inventory.isLonely()) {
			return;
		}
		if (inGui) {
			for (BodyPart part : playerRenderer.parts) {
				matrixStack.push();
				//float f = 1F / 0.9375F;
				//matrixStack.scale(f, f, f);
				part.applyRotationAndPos(matrixStack);
				setTransform(part.segment, matrixStack.getLast());
				matrixStack.pop();
			}
		}
		for (int i = 0; i < size; i++) {
			ItemStack stack = inventory.equipmentSlots.get(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ILonelyItem) {
					ILonelyItem lonelyItem = (ILonelyItem) stack.getItem();
					matrixStack.push();
					lonelyItem.getRenderer().renderOnPlayer(stack, TransformType.NONE, matrixStack, buffer, packedLight,
							OverlayTexture.NO_OVERLAY, partialTicks, i, player, playerRenderer);
					matrixStack.pop();
				}
			}
		}
	}

}
