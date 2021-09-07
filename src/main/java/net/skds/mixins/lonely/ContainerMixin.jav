package net.skds.mixins.lonely;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.skds.lonely.inventory.EContainer;
import net.skds.lonely.util.extended.EPlayerInventory;

@Mixin(Container.class)
public abstract class ContainerMixin {

	@Shadow
	private NonNullList<ItemStack> inventoryItemStacks;
	@Shadow
	public List<Slot> inventorySlots;

	@Overwrite
	protected Slot addSlot(Slot slotIn) {
		if (slotIn.inventory instanceof EPlayerInventory && !((Object) this instanceof EContainer)) {
			if (((EPlayerInventory) slotIn.inventory).isLonely() && slotIn.getSlotIndex() >= 2) {

				slotIn = new Slot(slotIn.inventory, slotIn.getSlotIndex(), -1488666, -1488666);
			}			
		}
		slotIn.slotNumber = this.inventorySlots.size();
		this.inventorySlots.add(slotIn);
		this.inventoryItemStacks.add(ItemStack.EMPTY);
		return slotIn;
	}
}
