package net.skds.lonely.client.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.skds.lonely.Lonely;

@OnlyIn(value = Dist.CLIENT)
public class ModelReg {
	
	public static final ResourceLocation BACKPACK = reg("staff/backpack");
	public static final ResourceLocation MISSING = reg("pudge");

	private static ResourceLocation reg(String path) {		
		ResourceLocation location = new ResourceLocation(Lonely.MOD_ID, path);
		ModelLoader.addSpecialModel(location);
		return location;
	}

	public static void init() {
	}

	public static IBakedModel get(ResourceLocation rl) {
		IBakedModel ibm = ModelLoader.instance().getTopBakedModels().get(rl);
		if (ibm == null) {
			return Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(new ItemStack(Items.BARRIER), null, null);
		}
		return ibm;
	}
}
