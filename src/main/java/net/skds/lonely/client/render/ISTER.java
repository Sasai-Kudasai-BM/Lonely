package net.skds.lonely.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.lonely.item.ILonelyItem;

@OnlyIn(Dist.CLIENT)
public class ISTER extends ItemStackTileEntityRenderer {

	private final Minecraft mc;

	private static ISTER instance = null;

	private ISTER() {
		this.mc = Minecraft.getInstance();
	}

	public static ISTER get() {
		if (instance == null) {
			instance = new ISTER();
		}
		return instance;
	}

	@Override
	public void func_239207_a_(ItemStack stack, TransformType tt, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		float partialTicks = mc.isGamePaused() ? 1 : mc.getRenderPartialTicks();
		Item item = stack.getItem();
		matrixStack.push();
		matrixStack.translate(0.5, 0.5, 0.5);
		if (item instanceof ILonelyItem) {
			ILonelyItem lonelyItem = (ILonelyItem) item;
			LonelyItemRenderer<?> lir = lonelyItem.getRenderer();
			lir.render(stack, tt, matrixStack, buffer, combinedLight, combinedOverlay, partialTicks);
			/*
			if (lir == null) {
				matrixStack.push();
				matrixStack.rotate(new Quaternion(Vector3f.YP, ((float) mc.world.getGameTime() + partialTicks) * 20, true));

				IBakedModel missiing = ModelReg.get(ModelReg.MISSING);
				if (missiing != null) {
					mc.getItemRenderer().renderItem(stack, tt, false, matrixStack, buffer, combinedLight, combinedOverlay, missiing);
				}
				matrixStack.pop();
			} else {
				
			}
			*/
		}
		matrixStack.pop();
	}
}