package net.skds.lonely.item.items;

import net.minecraft.item.Item;
import net.skds.lonely.client.render.LonelyItemRenderer;
import net.skds.lonely.item.ILonelyItem;
import net.skds.lonely.reg.RegItems;

public class BackpackItem extends Item implements ILonelyItem {

	public BackpackItem() {
		this(new Properties());
	}

	public BackpackItem(Properties properties) {
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

	@Override
	public LonelyItemRenderer<BackpackItem> getRenderer() {
		return null;
	}
	
	@Override
	public int getSize() {
		return 0;
	}
}
