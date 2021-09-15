package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.skds.lonely.inventory.EPlayerInventory;
import net.skds.lonely.item.ILonelyItem;

public class EquipmentLayerRenderer<T extends PlayerEntity, M extends PlayerModel<T> & IHasArm>
		extends LayerRenderer<T, M> {

	@SuppressWarnings("unused")
	private static final Minecraft mc = Minecraft.getInstance();
	private final EPlayerRenderer playerRenderer;

	@SuppressWarnings("unchecked")
	public EquipmentLayerRenderer(EPlayerRenderer playerRenderer) {
		super((IEntityRenderer<T, M>) playerRenderer);
		this.playerRenderer = playerRenderer;
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T player, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		EPlayerInventory inventory = (EPlayerInventory) player.inventory;
		if (!inventory.isLonely()) {
			return;
		}
		//System.out.println(inventory.equipmentSlots);
		int s = inventory.equipmentSlots.size();
		for (int i = 0; i < s; i++) {
			ItemStack stack = inventory.equipmentSlots.get(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ILonelyItem) {
					ILonelyItem lonelyItem = (ILonelyItem) stack.getItem();
					matrixStack.push();
					PlayerModel<AbstractClientPlayerEntity> model = playerRenderer.getEntityModel();

					
					matrixStack.translate(model.bipedBody.rotationPointX / 16.0F, model.bipedBody.rotationPointY / 16.0F, model.bipedBody.rotationPointZ / 16.0F);
					Quaternion q = new Quaternion(Vector3f.ZP, model.bipedBody.rotateAngleZ, false);
					q.multiply(new Quaternion(Vector3f.YP, model.bipedBody.rotateAngleY, false));
					q.multiply(new Quaternion(Vector3f.XP, model.bipedBody.rotateAngleX, false));

					matrixStack.rotate(q);
					matrixStack.translate(0.0D, 1.501F, 0.0D);
					float f = 1.066666F;
					//matrixStack.scale(-1.0F, -1.0F, 1.0F);
					matrixStack.scale(-f, -f, f);
					//matrixStack.rotate(new Quaternion(Vector3f.YP, 180, true));
					lonelyItem.getRenderer().renderOnPlayer(stack, TransformType.NONE, matrixStack, buffer, packedLight,
							OverlayTexture.NO_OVERLAY, partialTicks, i, player, model);
					matrixStack.pop();
				}
			}
		}
	}

}
