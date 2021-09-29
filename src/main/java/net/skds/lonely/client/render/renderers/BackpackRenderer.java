package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.Pair;
import net.skds.lonely.client.inventory.BodyPart;
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

		mc.getItemRenderer().renderItem(stack, trans, false, matrixStack, buffer, combinedLight, combinedOverlay,
				ModelReg.get(ModelReg.BACKPACK));

	}

	@Override
	public void renderOnPlayer(ItemStack stack, TransformType trans, MatrixStack matrixStack, IRenderTypeBuffer buffer,
			int combinedLight, int combinedOverlay, float partialTicks, int eqSlot, PlayerEntity player, EPlayerRenderer renderer) {
		matrixStack.push();

		Pair<Matrix3f, Matrix4f> pair = EquipmentLayerRenderer.getTransform(BodyPart.Segment.BODY);

		MatrixStack ms2;
		if (EquipmentLayerRenderer.inGui) {
			ms2 = new MatrixStack();
			ms2.push();
			MatrixStack.Entry ms2Entry = ms2.getLast();
			ms2Entry.getNormal().mul(pair.a);
			ms2Entry.getMatrix().mul(pair.b);
		} else {
			matrixStack.rotate(new Quaternion(Vector3f.XP, 180, true));
			ms2 = matrixStack;
		}
		//ms2.translate(0.5F, 0.5F, 0.5F);
		render(stack, trans, ms2, buffer, combinedLight, combinedOverlay, partialTicks);
		matrixStack.pop();
	}
}
