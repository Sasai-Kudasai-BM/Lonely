package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.lonely.entity.LonelyItemEntity;

@OnlyIn(Dist.CLIENT)
public class LonelyItemEntityRenderer extends EntityRenderer<LonelyItemEntity> {
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

	public void render(LonelyItemEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn) {

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

	}

	@SuppressWarnings("deprecation")
	public ResourceLocation getEntityTexture(LonelyItemEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
