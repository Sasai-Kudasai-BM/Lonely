package net.skds.lonely.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.OBBShape;
import net.skds.lonely.client.inventory.ClickOBBShape;
import net.skds.lonely.client.inventory.BodyPart.Segment;
import net.skds.lonely.inventory.EquipmentLayer;

public interface ILonelyEquipItem extends ILonelyItem {

	@OnlyIn(Dist.CLIENT)
	public Map<Segment, OBBShape> getShapeMap(ItemStack stack, EquipmentLayer layer);

	@OnlyIn(Dist.CLIENT)
	public default List<ClickOBBShape> getClickShapeList(ItemStack stack, EquipmentLayer layer) {
		Map<Segment, OBBShape> map = getShapeMap(stack, layer);
		List<ClickOBBShape> outputMap = new ArrayList<>();
		map.forEach((seg, shape) -> {
			outputMap.add(new ClickOBBShape(shape.getBoxes(), layer, stack, seg));
		});
		return outputMap;
	}

	public List<EquipmentLayer> getLayers(ItemStack stack);
}
