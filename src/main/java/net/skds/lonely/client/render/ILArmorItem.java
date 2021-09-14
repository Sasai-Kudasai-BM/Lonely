package net.skds.lonely.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface ILArmorItem {
	
	public void renderOnPlayer(PlayerEntity player, ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, float partialTicks);
}
