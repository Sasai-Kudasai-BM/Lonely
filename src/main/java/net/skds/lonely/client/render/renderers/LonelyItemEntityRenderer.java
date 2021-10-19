package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.OBB;
import net.skds.lonely.entity.LonelyItemEntity;
import net.skds.lonely.item.ILonelyItem;

@OnlyIn(Dist.CLIENT)
public class LonelyItemEntityRenderer extends EntityRenderer<LonelyItemEntity> {

	private final Minecraft mc = Minecraft.getInstance();

	public LonelyItemEntityRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.5F;
	}

	@Override
	public boolean shouldRender(LonelyItemEntity livingEntityIn, ClippingHelper camera, double camX, double camY,
			double camZ) {
		return super.shouldRender(livingEntityIn, camera, camX, camY, camZ);
	}

	@Override
	public Vector3d getRenderOffset(LonelyItemEntity entityIn, float partialTicks) {
		return super.getRenderOffset(entityIn, partialTicks);
	}

	public void render(LonelyItemEntity lonelyItemEntity, float entityYaw, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int packedLight) {
		super.render(lonelyItemEntity, entityYaw, partialTicks, matrixStack, buffer, packedLight);

		ItemStack stack = lonelyItemEntity.itemStack;

		if (!stack.isEmpty()) {
			mc.getItemRenderer().renderItem(lonelyItemEntity.itemStack, TransformType.NONE, packedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);

			if (stack.getItem() instanceof ILonelyItem) {
				IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.LINES);
				for (OBB obb : ((ILonelyItem) stack.getItem()).getShape(stack).getBoxes()) {
					obb.render(matrixStack, vertexBuilder, 1, 0, 1, 1);
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	public ResourceLocation getEntityTexture(LonelyItemEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
