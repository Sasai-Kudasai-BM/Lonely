package net.skds.lonely.item;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.lonely.client.render.LonelyItemRenderer;

public interface ILonelyItem {

	public default float getMass() {
		return getDryMass();
	}

	public float getDryMass();

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

}
