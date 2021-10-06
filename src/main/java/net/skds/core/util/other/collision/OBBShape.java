package net.skds.core.util.other.collision;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.AxisAlignedBB;

public class OBBShape {

	public static final OBBShape EMPTY = new OBBShape(new ArrayList<>());

	protected AxisAlignedBB boundingBox;
	protected List<OBB> boxes;

	public OBBShape(List<OBB> boxes) {
		this.boxes = boxes;
		if (boxes.isEmpty()) {
			boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
		} else {
			boundingBox = boxes.get(0).aabb;
			if (boxes.size() > 1) {
				for (int i = 1; i < boxes.size(); i++) {					
					boundingBox = boundingBox.union(boxes.get(i).aabb);
				}
			}
		}
	}

	public List<OBB> getBoxes() {
		return boxes;
	}

	public boolean isEmpty() {
		return boxes.isEmpty();
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}
}
