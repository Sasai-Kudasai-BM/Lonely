package net.skds.lonely.item.items;

import net.minecraft.item.Item;
import net.skds.lonely.item.ILonelyItem;
import net.skds.lonely.reg.RegItems;

public class BackPackItem extends Item implements ILonelyItem {

	public BackPackItem() {
		this(new Properties());
	}

	public BackPackItem(Properties properties) {
		super(properties.group(RegItems.CTAB));
	}

	@Override
	public float getDryMass() {
		return 0.5F;
	}

	@Override
	public String getId() {
		return "backpack";
	}
	
}
