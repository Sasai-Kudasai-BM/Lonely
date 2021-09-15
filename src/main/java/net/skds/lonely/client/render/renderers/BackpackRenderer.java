package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.OBB;
import net.skds.lonely.client.models.ModelReg;
import net.skds.lonely.client.render.LonelyItemRenderer;
import net.skds.lonely.item.items.BackpackItem;

@OnlyIn(Dist.CLIENT)
public class BackpackRenderer extends LonelyItemRenderer<BackpackItem> {

	private static BackpackRenderer instance = null;

	public static BackpackRenderer get() {
		if (instance == null) {
			instance = new BackpackRenderer();
		}
		return instance;
	}

	@Override
	public void render(ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay, float partialTicks) {
		//Entity entity = stack.getAttachedEntity();
		//System.out.println(entity);

		mc.getItemRenderer().renderItem(stack, trans, false, matrixStack, buffer, combinedLight, combinedOverlay,
				ModelReg.get(ModelReg.BACKPACK));

	}

	@Override
	public void renderOnPlayer(ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay, float partialTicks, int eqSlot, PlayerEntity player, PlayerModel<AbstractClientPlayerEntity> model) {
		matrixStack.push();

		BackpackItem backpackItem = (BackpackItem) stack.getItem();

		
		IVertexBuilder builder = buffer.getBuffer(RenderType.LINES);
		for (OBB obb : backpackItem.getClickBoxes()) {
			obb.render(matrixStack.getLast().getMatrix(), builder, 0F, 1F, 0F, 1F);
		}
		
		render(stack, trans, matrixStack, buffer, combinedLight, combinedOverlay, partialTicks);
		matrixStack.pop();
	}
}
