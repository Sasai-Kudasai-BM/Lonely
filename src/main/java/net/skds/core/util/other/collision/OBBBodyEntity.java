package net.skds.core.util.other.collision;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.skds.core.register.RegSerializers;
import net.skds.core.util.other.collision.OBBBody.PhysData;

public abstract class OBBBodyEntity extends Entity {

	private OBBBody<? extends OBBBodyEntity> body;
	
	private static final DataParameter<PhysData> PHYS_DATA = EntityDataManager.createKey(OBBBodyEntity.class, RegSerializers.PHYS_SERIAL);


	public OBBBodyEntity(EntityType<?> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		body = new OBBBody<OBBBodyEntity>(this);
	}

	public abstract OBBShape getShape();

	public void setBody(OBBBody<? extends OBBBodyEntity> body) {
		this.body = body;		
	}

	public OBBBody<? extends OBBBodyEntity> getBody() {
		return body;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		getBody().pd.readAdditional(compound);

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		getBody().pd.writeAdditional(compound);
	}

	@Override
	public void tick() {
		body.tick();
	}

	@Override
	protected void registerData() {
		this.getDataManager().register(PHYS_DATA, new PhysData());		
	}
	
	public void syncData() {
		if (world.isRemote) {
			body.pd = dataManager.get(PHYS_DATA);
		} else {
			dataManager.set(PHYS_DATA, body.pd);
		}		
	}

}
