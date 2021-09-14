package net.skds.lonely.util.hooks;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.skds.lonely.inventory.EPlayerInventory;

public class PlayerHooks {

	public static ItemStack getItemStackFromSlot(EquipmentSlotType slotIn, PlayerEntity player) {
		if (slotIn == EquipmentSlotType.MAINHAND) {
			return player.inventory.getStackInSlot(0);
		} else if (slotIn == EquipmentSlotType.OFFHAND) {
			return player.inventory.getStackInSlot(1);
		} else {
			return ItemStack.EMPTY;
		}
	}

	public static void setItemStackToSlot(EquipmentSlotType slotIn, PlayerEntity player, ItemStack stack) {
		//System.out.println(stack + " " + slotIn);
		if (slotIn == EquipmentSlotType.MAINHAND) {	
			player.inventory.setInventorySlotContents(0, stack);		
		} else if (slotIn == EquipmentSlotType.OFFHAND) {
			player.inventory.setInventorySlotContents(1, stack);	
		}
	}

	
	public static void replaceItemInInventory(int inventorySlot, ItemStack itemStackIn, CallbackInfoReturnable<Boolean> ci, PlayerEntity player) {
		EPlayerInventory epl = (EPlayerInventory) player.inventory;
		if (epl.isLonely()) {
			epl.setInventorySlotContents(inventorySlot, itemStackIn);
			ci.setReturnValue(true);
		}
	}
}
