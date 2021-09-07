package net.skds.mixins.lonely;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.skds.lonely.inventory.EContainer;
import net.skds.lonely.util.extended.EPlayerInventory;

@Mixin(Slot.class)
public abstract class SlotMixin {

	@Shadow
	private int slotIndex;
	@Shadow
	public IInventory inventory;
	@Shadow
	public int xPos;

	private int xPosOld = 0;

	private static final ItemStack baaad = new ItemStack(Items.BARRIER);

	@Inject(method = "<init>", at = @At("TAIL"))
	void init(IInventory inventory, int index, int xPosition, int yPosition, CallbackInfo ci) {
		checkBadPidod();
	}

	private boolean checkBadPidod() {
		if (inventory instanceof EPlayerInventory && !((Object) this instanceof EContainer)) {
			if (((EPlayerInventory) inventory).isLonely() && slotIndex >= 2) {
				xPosOld = xPos;
				xPos = 6661488;
				return true;
			}			
		}
		if (xPos ==6661488) {
			xPos = xPosOld;
		}
		return false;
	}

	@Overwrite
	public ItemStack getStack() {
		if (checkBadPidod()) {
			return baaad;
		}
		return this.inventory.getStackInSlot(this.slotIndex);
	}
}
