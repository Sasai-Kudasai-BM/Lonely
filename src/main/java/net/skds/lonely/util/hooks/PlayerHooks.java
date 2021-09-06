package net.skds.lonely.util.hooks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.skds.lonely.util.extended.EPlayerInventory;

public class PlayerHooks {

	public static ItemStack getItemStackFromSlot(EquipmentSlotType slotIn, PlayerEntity player) {
		if (slotIn == EquipmentSlotType.MAINHAND) {			
			return ((EPlayerInventory) player.inventory).mainHand;
		} else if (slotIn == EquipmentSlotType.OFFHAND) {
			return ((EPlayerInventory) player.inventory).offHand;
		} else {
			return ItemStack.EMPTY;
		}
	}

	public static void setItemStackToSlot(EquipmentSlotType slotIn, PlayerEntity player, ItemStack stack) {
		if (slotIn == EquipmentSlotType.MAINHAND) {	
			((EPlayerInventory) player.inventory).mainHand = stack;		
		} else if (slotIn == EquipmentSlotType.OFFHAND) {
			((EPlayerInventory) player.inventory).offHand = stack;		
		}
	}
}
