package net.skds.core.register;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.skds.core.SKDSCore;
import net.skds.core.util.other.collision.OBBBody;
import net.skds.core.util.other.collision.OBBBody.PhysDataSerializer;

public class RegSerializers {

	
	public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, SKDSCore.MOD_ID);

	public static final PhysDataSerializer PHYS_SERIAL = new OBBBody.PhysDataSerializer();
	public static final RegistryObject<DataSerializerEntry> LONELY_ITEM = SERIALIZERS.register("phys_data", () -> new DataSerializerEntry(PHYS_SERIAL));


	public static void reg(IEventBus eventBus) {
		SERIALIZERS.register(eventBus);
	}
}
