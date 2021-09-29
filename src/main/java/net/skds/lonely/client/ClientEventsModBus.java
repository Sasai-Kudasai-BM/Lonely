package net.skds.lonely.client;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
import net.skds.lonely.client.render.renderers.LonelyItemEntityRenderer;
import net.skds.lonely.entity.LonelyItemEntity;
import net.skds.lonely.reg.ContTypes;
import net.skds.lonely.reg.RegEntity;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Lonely.MOD_ID, bus = Bus.MOD)
public class ClientEventsModBus {

	@SuppressWarnings("unchecked")
	public static void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler((EntityType<LonelyItemEntity>) RegEntity.LONELY_ITEM.get(), LonelyItemEntityRenderer::new);
	}
	
	@SubscribeEvent
	public static void setupClient(final FMLClientSetupEvent event) {
		ModelReg.init();
		ScreenManager.registerFactory(ContTypes.INV_CONT.get(), EGui::new);
		registerRenderers();

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

	public static void initClient() {
		//ModelReg.init();
	}

}
