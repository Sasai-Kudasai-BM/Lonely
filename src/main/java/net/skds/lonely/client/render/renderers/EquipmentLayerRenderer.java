package net.skds.lonely.client.render.renderers;

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
import net.skds.lonely.inventory.EPlayerInventory;
import net.skds.lonely.item.ILonelyItem;

public class EquipmentLayerRenderer<T extends PlayerEntity, M extends PlayerModel<T> & IHasArm> extends LayerRenderer<T, M> {

	private static final Minecraft mc = Minecraft.getInstance();
	private final EPlayerRenderer playerRenderer;

	@SuppressWarnings("unchecked")
	public EquipmentLayerRenderer(EPlayerRenderer playerRenderer) {
		super((IEntityRenderer<T, M>) playerRenderer);
		this.playerRenderer = playerRenderer;
	}

	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T player,
			float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
			float headPitch) {
		EPlayerInventory inventory = (EPlayerInventory) player.inventory;
		if (!inventory.isLonely()) {
			return;
		}
		int s = inventory.equipmentSlots.size();
		for (int i = 0; i < s; i++) {
			ItemStack stack = inventory.equipmentSlots.get(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ILonelyItem) {
					ILonelyItem lonelyItem = (ILonelyItem) stack.getItem();
					lonelyItem.getRenderer().render(stack, TransformType.NONE, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, partialTicks);
				}
			}
		}
	}
	
}
