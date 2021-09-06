package net.skds.lonely.util.extended;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.skds.lonely.Lonely;

public class EPlayerContainer extends PlayerContainer {

	private boolean allowAdd = false;

	public final PlayerEntity player;   
	private final CraftingInventory craftMatrix = new CraftingInventory(this, 2, 2);
	private final CraftResultInventory craftResult = new CraftResultInventory();

	public EPlayerContainer(PlayerInventory playerInventory, boolean localWorld, PlayerEntity playerIn) {
		super(playerInventory, localWorld, playerIn);
		this.player = playerIn;
		allowAdd = true;

		
		this.addSlot(new Slot(playerInventory, 0, 180, 142));
		this.addSlot(new Slot(playerInventory, 1, 200, 142));
		
		//this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));
		//for (int i = 0; i < 2; ++i) {
		//	for (int j = 0; j < 2; ++j) {
		//		this.addSlot(new Slot(this.craftMatrix, j + i * 2, 98 + j * 18, 18 + i * 18));
		//	}
		//}
	}

	@Override
	protected Slot addSlot(Slot slotIn) {
		System.out.println(allowAdd);
		if (allowAdd) {
			return super.addSlot(slotIn);
		}
		return null;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		return super.transferStackInSlot(playerIn, index);
	}

	@Override
	public Slot getSlot(int slotId) {

		System.out.println(slotId);
		//if (slotId >= player.inventory.getSizeInventory()) {
		//	slotId = 0;
		//}
		return super.getSlot(slotId);
	}

}
