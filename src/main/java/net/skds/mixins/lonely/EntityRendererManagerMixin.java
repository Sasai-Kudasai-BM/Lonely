package net.skds.mixins.lonely;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.skds.lonely.client.render.renderers.EPlayerRenderer;

@Mixin(EntityRendererManager.class)
public class EntityRendererManagerMixin {

	@Shadow
	private Map<String, PlayerRenderer> skinMap;
	@Shadow
	private PlayerRenderer playerRenderer;

	@Inject(method = "<init>", at = @At("TAIL"))
	void init(TextureManager textureManagerIn, ItemRenderer itemRendererIn, IReloadableResourceManager resourceManagerIn, FontRenderer fontRendererIn, GameSettings gameSettingsIn, CallbackInfo ci) {

		this.playerRenderer = new EPlayerRenderer((EntityRendererManager) (Object) this, false);
		this.skinMap.put("default", this.playerRenderer);
		this.skinMap.put("slim", new EPlayerRenderer((EntityRendererManager) (Object) this, true));
	}
	
}
