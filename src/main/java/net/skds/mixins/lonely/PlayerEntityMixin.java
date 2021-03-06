package net.skds.mixins.lonely;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.skds.lonely.inventory.EPlayerInventory;
import net.skds.lonely.util.hooks.PlayerHooks;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow
	public final PlayerInventory inventory = new EPlayerInventory((PlayerEntity) (Object) this);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	public void cont(World w, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
		//PlayerEntity p = (PlayerEntity) (Object) this;
	}

	@Inject(method = "replaceItemInInventory", at = @At("HEAD"), cancellable = true)
	void replaceItemInInventory(int inventorySlot, ItemStack itemStackIn, CallbackInfoReturnable<Boolean> ci) {
		PlayerHooks.replaceItemInInventory(inventorySlot, itemStackIn, ci, (PlayerEntity) (Object) this);
	}

	@Inject(method = "getItemStackFromSlot", at = @At("HEAD"), cancellable = true)
	public void getItemStackFromSlot(EquipmentSlotType slotIn, CallbackInfoReturnable<ItemStack> ci) {
		PlayerEntity p = (PlayerEntity) (Object) this;
		if (p.abilities.isCreativeMode) {
			return;
		}
		ci.setReturnValue(PlayerHooks.getItemStackFromSlot(slotIn, (PlayerEntity) (Object) this));
	}

	@Inject(method = "setItemStackToSlot", at = @At("HEAD"), cancellable = true)
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack, CallbackInfo ci) {
		PlayerEntity p = (PlayerEntity) (Object) this;
		if (p.abilities.isCreativeMode) {
			return;
		}
		if (slotIn == EquipmentSlotType.MAINHAND) {
			this.playEquipSound(stack);
			PlayerHooks.setItemStackToSlot(slotIn, (PlayerEntity) (Object) this, stack);
		} else if (slotIn == EquipmentSlotType.OFFHAND) {
			this.playEquipSound(stack);
			PlayerHooks.setItemStackToSlot(slotIn, (PlayerEntity) (Object) this, stack);
		} else if (slotIn.getSlotType() == EquipmentSlotType.Group.ARMOR) {
			this.playEquipSound(stack);
			PlayerHooks.setItemStackToSlot(slotIn, (PlayerEntity) (Object) this, stack);
		}
		ci.cancel();
	}
}
