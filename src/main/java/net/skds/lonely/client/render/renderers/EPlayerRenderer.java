package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EPlayerRenderer extends PlayerRenderer {

	@SuppressWarnings("unused")
	private static final Minecraft mc = Minecraft.getInstance();

	public EPlayerRenderer(EntityRendererManager renderManager, boolean useSmallArms) {
		super(renderManager, useSmallArms);
		addLayer(new EquipmentLayerRenderer<>(this));
	}

	@Override
	public void render(AbstractClientPlayerEntity player, float entityYaw, float partialTicks,
			MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
		super.render(player, entityYaw, partialTicks, matrixStack, buffer, packedLight);

		renderEquipment(player, partialTicks, matrixStack, buffer, packedLight);
	}

	private void renderEquipment(AbstractClientPlayerEntity player, float partialTicks,
	MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {

	}

	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayerEntity player) {
		return player.getLocationSkin();
	}

}
