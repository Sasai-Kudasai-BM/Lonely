package net.skds.lonely.util.extended;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;

public class EPlayerContainer extends PlayerContainer {

	public EPlayerContainer(PlayerInventory playerInventory, boolean localWorld, PlayerEntity playerIn) {
		super(playerInventory, localWorld, playerIn);
		System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		// TODO Auto-generated method stub
		return super.transferStackInSlot(playerIn, index);
	}
	
}
