package net.skds.lonely.item.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.OBB;
import net.skds.lonely.client.render.LonelyItemRenderer;
import net.skds.lonely.client.render.renderers.BackpackRenderer;
import net.skds.lonely.item.ILonelyEquipItem;
import net.skds.lonely.reg.RegItems;

public class BackpackItem extends Item implements ILonelyEquipItem {

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
	@OnlyIn(Dist.CLIENT)
	public LonelyItemRenderer<BackpackItem> getRenderer() {
		return BackpackRenderer.get();
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public List<OBB> getClickBoxes() {
		List<OBB> list = new ArrayList<>();

		AxisAlignedBB box = new AxisAlignedBB(-0.5, 0.0, -0.2, -0.2, 0.5, 0.2);
		OBB obb = new OBB(box);
		list.add(obb);

		return list;
	}
}
