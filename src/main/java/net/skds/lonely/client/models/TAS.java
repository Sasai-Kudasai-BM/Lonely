package net.skds.lonely.client.models;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.lonely.Lonely;

@OnlyIn(value = Dist.CLIENT)
public class TAS extends TextureAtlasSprite {

	protected TAS(AtlasTexture atlasTextureIn, Info spriteInfoIn, int atlasWidthIn,
			int atlasHeightIn, NativeImage imageIn) {
		super(atlasTextureIn, spriteInfoIn, 0, atlasWidthIn, atlasHeightIn, 0, 0, imageIn);
	}

	@SuppressWarnings("deprecation")
	public static TextureAtlasSprite getSprite(ResourceLocation rl) {
		IResource iresource;
		try {
			iresource = Minecraft.getInstance().getResourceManager().getResource(rl);
			NativeImage nativeimage = NativeImage.read(iresource.getInputStream());
			int h = nativeimage.getHeight();
			int w = nativeimage.getWidth();
	
			return new TAS(new AtlasTexture(rl), new Info(rl, w, h, AnimationMetadataSection.EMPTY), w, h, nativeimage);
		} catch (IOException e) {
			Lonely.LOGGER.error("No texture! " + rl);
			return new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation("block/barrier")).getSprite();
		}
	}	
}
