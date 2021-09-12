package net.skds.lonely.client.models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.ModelSettings;
import net.skds.lonely.Lonely;

@OnlyIn(value = Dist.CLIENT)
public class LModelLoader extends OBJLoader {

	public static LModelLoader INSTANCE = new LModelLoader();

	final Minecraft mc = Minecraft.getInstance();
	private final Map<ResourceLocation, LBakedModel> lBakedModels = new HashMap<>();

	public LModelLoader() {
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		super.onResourceManagerReload(resourceManager);
		
		Lonely.LOGGER.info("Registering models");
		lBakedModels.clear();

		resourceManager.getAllResourceLocations("objmodels", s -> s.endsWith(".obj")).forEach(rl -> {

			try {
				ResourceLocation rl2 = new ResourceLocation(rl.getNamespace(), rl.getPath().replace(".obj", ""));
				String override = null;

				if (rl2.getPath().contains("animated")) {					
					override = "main.mtl";
				}

				OBJModel obj = loadModel(new ModelSettings(rl, false, false, true, false, override));
				LBakedModel kBModel = new LBakedModel(obj, rl);	
				lBakedModels.put(rl2, kBModel);
				Lonely.LOGGER.warn("Imported Model at path %s", rl2);
			} catch (Exception e) {
				Lonely.LOGGER.error("OBJ Error\n", e);
			}

		});
	}

	public void bakeModels() {
		Lonely.LOGGER.info("Baking models");

		lBakedModels.forEach((rl, kBModel) -> {
			try {
				kBModel.bake();
				Lonely.LOGGER.warn("Baked Model at path %s", rl);
			} catch (Exception e) {
				Lonely.LOGGER.error("Bakery Error\n", e);
			}
		});
	}

	@Nullable
	public LBakedModel getBakedModel(ResourceLocation rl) {
		return lBakedModels.get(rl);
	}

	@Nullable
	public LBakedModel getBakedModel(String name) {
		return lBakedModels.get(new ResourceLocation(Lonely.MOD_ID, "objmodels/" + name));
	}

	@Override
	public OBJModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
		return super.read(deserializationContext, modelContents);
	}
}
