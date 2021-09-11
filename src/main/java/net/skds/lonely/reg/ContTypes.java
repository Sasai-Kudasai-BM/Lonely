package net.skds.lonely.reg;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.skds.lonely.Lonely;
import net.skds.lonely.inventory.EContainer;

public class ContTypes {

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Lonely.MOD_ID);

	public static final RegistryObject<ContainerType<EContainer>> INV_CONT = register("inv_cont", (windowId, inv, data) -> new EContainer(ContTypes.INV_CONT.get(), windowId, inv));

	
	private static <C extends Container> RegistryObject<ContainerType<C>> register(String name, IContainerFactory<C> factory) {
		return CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
	}

	public static void reg(IEventBus eventBus) {
		CONTAINERS.register(eventBus);
	}

}
