package net.skds.lonely.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class EContainer extends Container {

	//public EInventory inv;
	public PlayerInventory inv;

	public EContainer(PlayerEntity player) {
		super(null, 0);
		//inv = new EInventory(player);
		inv = player.inventory;
		addSlot(new Slot(inv, 0, 0, 0));
		addSlot(new Slot(inv, 1, 77, 62));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	@Override
	public Slot getSlot(int slotId) {
		//if (slotId >= 2) {
		//	slotId = 0;
		//}
		System.out.println(slotId);
		return super.getSlot(slotId);
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		//slotId -= 36;
		//if (slotId >= 2 || slotId < 0) {
		//	slotId = 0;
		//}
		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
		//System.out.println(player.openContainer);
		return stack;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		//return super.transferStackInSlot(playerIn, index);
		//System.out.println(index);
		return ItemStack.EMPTY;
	}
	

	//@Override
	//public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
	//	return super.canMergeSlot(stack, slotIn);
	//}

}
