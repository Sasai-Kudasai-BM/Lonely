package net.skds.lonely.client.render.renderers;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.lonely.client.inventory.BodyPart;

@OnlyIn(Dist.CLIENT)
public class EPlayerRenderer extends PlayerRenderer {

	@SuppressWarnings("unused")
	private static final Minecraft mc = Minecraft.getInstance();
	public final List<BodyPart> parts = new ArrayList<>();

	public EPlayerRenderer(EntityRendererManager renderManager, boolean useSmallArms) {
		super(renderManager, useSmallArms);
		addLayer(new EquipmentLayerRenderer<>(this));
		PlayerModel<AbstractClientPlayerEntity> model = getEntityModel();
		for (BodyPart.Segment segment : BodyPart.Segment.values()) {
			BodyPart.create(segment, model, parts);
		}
	}

	@Override
	public void render(AbstractClientPlayerEntity player, float entityYaw, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int packedLight) {
		super.render(player, entityYaw, partialTicks, matrixStack, buffer, packedLight);

		renderEquipment(player, partialTicks, matrixStack, buffer, packedLight);
	}

	private void renderEquipment(AbstractClientPlayerEntity player, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer buffer, int packedLight) {

	}

	@Override
	public ResourceLocation getEntityTexture(AbstractClientPlayerEntity player) {
		return player.getLocationSkin();
	}

	public void applyRotationsC(AbstractClientPlayerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	public void setModelVisibilitiesC(AbstractClientPlayerEntity clientPlayer) {
		PlayerModel<AbstractClientPlayerEntity> playermodel = this.getEntityModel();

		playermodel.isSneak = clientPlayer.isCrouching();

	}
}
