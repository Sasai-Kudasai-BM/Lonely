package net.skds.lonely.item;

public interface ILonelyItem {
	
	public default float getMass() {
		return getDryMass();
	}
	public float getDryMass();
	public String getId();

}
