package net.skds.lonely.client;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.skds.lonely.Lonely;
import net.skds.lonely.client.bbreader.BBParser;
import net.skds.lonely.client.inventory.EGui;
import net.skds.lonely.client.models.LModelLoader;
import net.skds.lonely.client.models.ModelReg;
import net.skds.lonely.reg.ContTypes;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Lonely.MOD_ID, bus = Bus.MOD)
public class ClientEventsModBus {
	
	@SubscribeEvent
	public static void setupClient(final FMLClientSetupEvent event) {
		ModelReg.init();
		ScreenManager.registerFactory(ContTypes.INV_CONT.get(), EGui::new);

		((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(reloaader);
	}

	@SubscribeEvent
	public static void modelBake(ModelBakeEvent e) {
		LModelLoader.INSTANCE.bakeModels();
		
		//LModelLoader.INSTANCE.onResourceManagerReload(Minecraft.getInstance().getResourceManager());
	}

	
	public static ISelectiveResourceReloadListener reloaader = new ISelectiveResourceReloadListener() {
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager,
				Predicate<IResourceType> resourcePredicate) {
			LModelLoader.INSTANCE.onResourceManagerReload(resourceManager);
			BBParser.read();
		}
	};

}
