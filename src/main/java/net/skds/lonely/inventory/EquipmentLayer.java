package net.skds.lonely.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public enum EquipmentLayer {
	BOOTS(1, 0),
	UNDERPANTS(2, -1),
	PANTS(2, 0),
	OVERPANTS(2, 1),
	SHIRT(4, 0),
	JACKET(4, 1),
	BODYARMOR(4, 2),
	GLOVES(8, 0),
	HELMET(16, 0),
	MASK(16, 0),
	BELT(32, 0),
	BACKPACK(64, 5);

	public final int priority;
	public final int position;

	private EquipmentLayer(int position, int priority) {
		this.priority = priority;
		this.position = position;
	}

	public boolean canInteract(NonNullList<ItemStack> equipmentSlots) {
		for (EquipmentLayer layer : values()) {
			boolean b = (layer.position & this.position) != 0;
			b = b && layer.isOccupied(equipmentSlots);
			b = b && layer.priority > this.priority;
			if (b) {
				return false;
			}
		}
		return true;		
	}

	public boolean isOccupied(NonNullList<ItemStack> equipmentSlots) {
		return !equipmentSlots.get(ordinal()).isEmpty();
	}

	public static EquipmentLayer get(int i) {
		return values()[i];
	}
}
