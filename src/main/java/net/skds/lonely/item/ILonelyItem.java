package net.skds.lonely.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.OBBShape;
import net.skds.lonely.client.render.LonelyItemRenderer;

public interface ILonelyItem {

	public default float getMass(ItemStack stack) {
		return getDryMass(stack);
	}

	public float getDryMass(ItemStack stack);

	public String getId();

	/**
	* | 6->1 
	* | 5->2 
	* | 4->4 
	* | 3->8 
	* | 2->16 
	* | 1->32 
	* | 0->64 
	* | -1->128|
	*/
	public int getSize();

	@OnlyIn(Dist.CLIENT)
	public LonelyItemRenderer<?> getRenderer();

	public OBBShape getShape(ItemStack stack);
}
