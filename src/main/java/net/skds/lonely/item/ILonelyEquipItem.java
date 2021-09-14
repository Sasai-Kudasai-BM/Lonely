package net.skds.lonely.item;

import java.util.List;

import net.skds.core.util.other.collision.OBB;

public interface ILonelyEquipItem extends ILonelyItem {

	public List<OBB> getClickBoxes();
}
