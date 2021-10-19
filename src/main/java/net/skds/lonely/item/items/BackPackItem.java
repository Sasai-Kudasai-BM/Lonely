package net.skds.lonely.item.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.BBParser;
import net.skds.core.util.other.collision.OBB;
import net.skds.core.util.other.collision.OBBShape;
import net.skds.lonely.client.inventory.BodyPart.Segment;
import net.skds.lonely.client.render.LonelyItemRenderer;
import net.skds.lonely.client.render.renderers.BackpackRenderer;
import net.skds.lonely.inventory.EquipmentLayer;
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
	public float getDryMass(ItemStack stack) {
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
	public OBBShape getShape(ItemStack stack) {
		List<OBB> list = BBParser.get("lonely/prikol");
		return new OBBShape(list);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Map<Segment, OBBShape> getShapeMap(ItemStack stack, EquipmentLayer layer) {
		Map<Segment, OBBShape> map = new HashMap<>();
		map.put(Segment.BODY, getShape(stack));
		//map.put(Segment.HEAD, getShape(stack));
		return map;
	}

	@Override
	public List<EquipmentLayer> getLayers(ItemStack stack) {
		List<EquipmentLayer> list = new ArrayList<>();
		list.add(EquipmentLayer.BACKPACK);
		return list;
	}
}
