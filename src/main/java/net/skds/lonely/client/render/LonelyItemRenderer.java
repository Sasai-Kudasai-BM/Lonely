package net.skds.lonely.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.lonely.item.ILonelyItem;

@OnlyIn(Dist.CLIENT)
public abstract class LonelyItemRenderer<T extends Item & ILonelyItem> {

	protected final Minecraft mc = Minecraft.getInstance();
	
	public LonelyItemRenderer() {
	}

	public abstract void render(ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, float partialTicks);

	public void render(ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, float partialTicks, int eqSlot, PlayerEntity player) {
		render(stack, trans, matrixStack, buffer, combinedLight, combinedOverlay, partialTicks);
	}
}
