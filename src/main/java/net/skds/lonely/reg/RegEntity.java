package net.skds.lonely.reg;

import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.skds.lonely.Lonely;
import net.skds.lonely.entity.LonelyItemEntity;

public class RegEntity {
	
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Lonely.MOD_ID);

	public static final RegistryObject<EntityType<?>> LONELY_ITEM = LonelyItemEntity.register(ENTITIES);

	public static void reg(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}
}
