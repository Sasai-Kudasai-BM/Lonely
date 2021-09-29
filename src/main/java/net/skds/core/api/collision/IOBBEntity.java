package net.skds.core.api.collision;

import net.skds.core.util.mat.Vec3;
import net.skds.core.util.other.collision.OBBBody;
import net.skds.core.util.other.collision.OBBShape;

public interface IOBBEntity {

	public OBBShape getShape();
	public OBBBody<?> getBody();

	public Vec3 getRotation();
	public Vec3 getCOM();
	public Vec3 getSpin();
	public Vec3 getMomentum();
	public double getMass();

	public void setRotation(Vec3 rotation);
	public void setCOM(Vec3 com);
	public void setSpin(Vec3 spin);
	public void setMomentum(Vec3 momentum);
	public void setMass(double mass);
	
	default public void tickBody() {
		getBody().tick();
	}

	default public void addRotation(Vec3 rotation) {
		setRotation(getRotation().add(rotation));
	}

	default public void addSpin(Vec3 spin) {
		setSpin(getSpin().add(spin));
	}
}
