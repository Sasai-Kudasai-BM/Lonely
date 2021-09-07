package net.skds.lonely;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.skds.core.events.PacketRegistryEvent;
import net.skds.lonely.network.OpenEGuiPacket;

@Mod(Lonely.MOD_ID)
public class Lonely {

    public static final String MOD_ID = "lonely";
    public static final String MOD_NAME = "Lonely";
	
	public static final Logger LOGGER = LogManager.getFormatterLogger(MOD_NAME);

	public Lonely() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::net);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

		MinecraftForge.EVENT_BUS.register(new Events());
		MinecraftForge.EVENT_BUS.register(this);
		//SKDSCoreConfig.init();
		//RegisterDebug.register();		
	}

	
	public void net(PacketRegistryEvent e) {
		e.registerPacket(OpenEGuiPacket.class, OpenEGuiPacket::encoder, OpenEGuiPacket::decoder, OpenEGuiPacket::handle);
	}

    private void setup(final FMLCommonSetupEvent event) {
    }

	private void setupClient(final FMLClientSetupEvent event) {
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
	}

    private void processIMC(final InterModProcessEvent event) {
    }
}
