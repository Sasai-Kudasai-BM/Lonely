package net.skds.lonely.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.skds.lonely.Lonely;
import net.skds.lonely.client.inventory.EGui;
import net.skds.lonely.reg.ContTypes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Lonely.MOD_ID, bus = Bus.MOD)
public class ClientEventsModBus {
	
	@SubscribeEvent
	public static void setupClient(final FMLClientSetupEvent event) {
		ScreenManager.registerFactory(ContTypes.INV_CONT.get(), EGui::new);
	}
}
