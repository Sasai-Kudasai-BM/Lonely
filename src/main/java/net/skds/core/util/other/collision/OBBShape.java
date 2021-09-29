package net.skds.core.util.other.collision;

import java.util.ArrayList;
import java.util.List;

public class OBBShape {

	public static final OBBShape EMPTY = new OBBShape(new ArrayList<>());
	
	protected List<OBB> boxes;

	public OBBShape(List<OBB> boxes) {
		this.boxes = boxes;
	}

	public List<OBB> getBoxes() {
		return boxes;
	}

	public boolean isEmpty() {
		return boxes.isEmpty();
	}
}
