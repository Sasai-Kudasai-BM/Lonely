package net.skds.lonely.client.render.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
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

	public void setModelVisibilities(AbstractClientPlayerEntity clientPlayer) {
		PlayerModel<AbstractClientPlayerEntity> playermodel = this.getEntityModel();
		if (clientPlayer.isSpectator()) {
			playermodel.setVisible(false);
			playermodel.bipedHead.showModel = true;
			playermodel.bipedHeadwear.showModel = true;
		} else {
			playermodel.setVisible(true);
			playermodel.bipedHeadwear.showModel = clientPlayer.isWearing(PlayerModelPart.HAT);
			playermodel.bipedBodyWear.showModel = clientPlayer.isWearing(PlayerModelPart.JACKET);
			playermodel.bipedLeftLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_PANTS_LEG);
			playermodel.bipedRightLegwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_PANTS_LEG);
			playermodel.bipedLeftArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.LEFT_SLEEVE);
			playermodel.bipedRightArmwear.showModel = clientPlayer.isWearing(PlayerModelPart.RIGHT_SLEEVE);
			playermodel.isSneak = clientPlayer.isCrouching();
			BipedModel.ArmPose bipedmodel$armpose = armPose(clientPlayer, Hand.MAIN_HAND);
			BipedModel.ArmPose bipedmodel$armpose1 = armPose(clientPlayer, Hand.OFF_HAND);
			if (bipedmodel$armpose.func_241657_a_()) {
				bipedmodel$armpose1 = clientPlayer.getHeldItemOffhand().isEmpty() ? BipedModel.ArmPose.EMPTY
						: BipedModel.ArmPose.ITEM;
			}

			if (clientPlayer.getPrimaryHand() == HandSide.RIGHT) {
				playermodel.rightArmPose = bipedmodel$armpose;
				playermodel.leftArmPose = bipedmodel$armpose1;
			} else {
				playermodel.rightArmPose = bipedmodel$armpose1;
				playermodel.leftArmPose = bipedmodel$armpose;
			}
		}

	}

	private static BipedModel.ArmPose armPose(AbstractClientPlayerEntity p_241741_0_, Hand p_241741_1_) {
		ItemStack itemstack = p_241741_0_.getHeldItem(p_241741_1_);
		if (itemstack.isEmpty()) {
			return BipedModel.ArmPose.EMPTY;
		} else {
			if (p_241741_0_.getActiveHand() == p_241741_1_ && p_241741_0_.getItemInUseCount() > 0) {
				UseAction useaction = itemstack.getUseAction();
				if (useaction == UseAction.BLOCK) {
					return BipedModel.ArmPose.BLOCK;
				}

				if (useaction == UseAction.BOW) {
					return BipedModel.ArmPose.BOW_AND_ARROW;
				}

				if (useaction == UseAction.SPEAR) {
					return BipedModel.ArmPose.THROW_SPEAR;
				}

				if (useaction == UseAction.CROSSBOW && p_241741_1_ == p_241741_0_.getActiveHand()) {
					return BipedModel.ArmPose.CROSSBOW_CHARGE;
				}
			} else if (!p_241741_0_.isSwingInProgress && itemstack.getItem() == Items.CROSSBOW
					&& CrossbowItem.isCharged(itemstack)) {
				return BipedModel.ArmPose.CROSSBOW_HOLD;
			}

			return BipedModel.ArmPose.ITEM;
		}
	}

}
