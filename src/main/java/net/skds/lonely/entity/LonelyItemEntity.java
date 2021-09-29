package net.skds.lonely.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.skds.core.util.other.collision.OBBBodyEntity;
import net.skds.lonely.reg.RegEntity;

public class LonelyItemEntity extends OBBBodyEntity {
	
	private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(LonelyItemEntity.class, DataSerializers.ITEMSTACK);

	public LonelyItemEntity(EntityType<?> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
	}

	public LonelyItemEntity(ItemEntity itemEntity) {
		super(RegEntity.LONELY_ITEM.get(), itemEntity.world);
		setPosition(itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ());
		setMotion(itemEntity.getMotion());
	}

	public static RegistryObject<EntityType<?>> register(DeferredRegister<EntityType<?>> registerer) {
		String id = "lonely_item";
		EntityType<?> type = EntityType.Builder.create(LonelyItemEntity::new, EntityClassification.MISC).size(1.0F, 1.0F).setTrackingRange(4)
				.setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build(id);
		return registerer.register(id, () -> type);
	}

	@Override
	public void tick() {
		//getBody().tick();
	}

	@Override
	protected void registerData() {
		this.getDataManager().register(ITEM, ItemStack.EMPTY);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
