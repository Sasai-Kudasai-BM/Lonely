package net.skds.lonely.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class EContainer extends Container {

	//public EInventory inv;
	public PlayerInventory inv;

	public EContainer(ContainerType<EContainer> type, int id, PlayerInventory inventory) {
		super(type, id);
		//inv = new EInventory(player);
		inv = inventory;
		addSlot(new Slot(inv, 1, 17, 104));
		addSlot(new Slot(inv, 0, 53, 104));
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}

	@Override
	public Slot getSlot(int slotId) {
		//System.out.println(slotId);
		return super.getSlot(slotId);
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
		ItemStack stack = super.slotClick(slotId, dragType, clickTypeIn, player);
		//System.out.println(slotId);
		return stack;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return ItemStack.EMPTY;
	}
}
