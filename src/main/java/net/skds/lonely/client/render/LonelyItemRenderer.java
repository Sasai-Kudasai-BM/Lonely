package net.skds.lonely.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.skds.lonely.item.ILonelyItem;

public abstract class LonelyItemRenderer<T extends Item & ILonelyItem> {
	
	public LonelyItemRenderer() {
	}

	public void render(ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {		
	}
}
