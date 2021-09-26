package net.skds.lonely.client.render.renderers;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.Pair;
import net.skds.core.util.other.collision.OBB;
import net.skds.lonely.client.inventory.BodyPart;
import net.skds.lonely.client.inventory.BodyPart.Segment;
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
			int combinedLight, int combinedOverlay, float partialTicks, int eqSlot, PlayerEntity player, EPlayerRenderer renderer) {
		//matrixStack.push();

		//renderer.parts.get(BodyPart.Segment.BODY).applyRotationAndPos(matrixStack);

		Pair<Matrix3f, Matrix4f> pair = EquipmentLayerRenderer.getTransform(BodyPart.Segment.BODY);

		MatrixStack ms2 = new MatrixStack();
		ms2.push();
		MatrixStack.Entry ms2Entry = ms2.getLast();
		ms2Entry.getNormal().mul(pair.a);
		ms2Entry.getMatrix().mul(pair.b);

		BackpackItem backpackItem = (BackpackItem) stack.getItem();

		
		IVertexBuilder builder = buffer.getBuffer(RenderType.LINES);
		ms2.push();
		float f = 1F / 0.9375F;
		ms2.scale(f, f, f);
		for (OBB obb : backpackItem.getClickBoxes()) {
			obb.render(ms2, builder, 0F, 1F, 0F, 1F);
		}
		ms2.pop();
		
		render(stack, trans, ms2, buffer, combinedLight, combinedOverlay, partialTicks);
		//matrixStack.pop();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<OBB> getClickBoxes4Segment(ItemStack stack, PlayerEntity player, Segment segment) {
		//if (segment == Segment.BODY || segment == Segment.RIGHT_ARM) {
		if (segment == Segment.BODY) {
			BackpackItem backpackItem = (BackpackItem) stack.getItem();
			return backpackItem.getClickBoxes();
		} else {
			return Collections.EMPTY_LIST;
		}
	}
}
