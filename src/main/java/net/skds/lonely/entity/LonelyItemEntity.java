package net.skds.lonely.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.skds.core.util.other.collision.BBParser;
import net.skds.core.util.other.collision.OBBBodyEntity;
import net.skds.core.util.other.collision.OBBShape;
import net.skds.lonely.reg.RegEntity;

public class LonelyItemEntity extends OBBBodyEntity {
	
	private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(LonelyItemEntity.class, DataSerializers.ITEMSTACK);

	public ItemStack itemStack = ItemStack.EMPTY;

	public LonelyItemEntity(EntityType<?> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
	}

	public LonelyItemEntity(ItemEntity itemEntity) {
		super(RegEntity.LONELY_ITEM.get(), itemEntity.world);
		setPosition(itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ());
		setMotion(itemEntity.getMotion());
		this.itemStack = itemEntity.getItem();
	}

	public static RegistryObject<EntityType<?>> register(DeferredRegister<EntityType<?>> registerer) {
		String id = "lonely_item";
		EntityType<?> type = EntityType.Builder.create(LonelyItemEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).setTrackingRange(4)
				.setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).build(id);
		return registerer.register(id, () -> type);
	}

	@Override
	public void tick() {
		super.tick();
		syncData();
	}

	@Override
	public void syncData() {
		super.syncData();
		if (world.isRemote) {
			itemStack = dataManager.get(ITEM);
		} else {
			dataManager.set(ITEM, itemStack);
		}
	}

	@Override
	public OBBShape getShape() {
		return new OBBShape(BBParser.get("lonely/prikol.bbmodel"));
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return getShape().getBoundingBox().offset(getPositionVec());
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(ITEM, ItemStack.EMPTY);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
