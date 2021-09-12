package net.skds.lonely.client.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.obj.OBJModel;

@OnlyIn(value = Dist.CLIENT)
public class LBakedModel implements IDynamicBakedModel {

	public final IModelTransform transform = new IModelTransform() {
	};
	public final ItemOverrideList overrideList = new ItemOverrideList() {
	};
	public final RenderMaterial material;
	public final ResourceLocation textureLocation;
	public TextureAtlasSprite sprite = null;

	private final String name;

	private final List<BakedQuad> quads = new ArrayList<>();
	private OBJModel obj = null;
	private final ResourceLocation location;

	public LBakedModel(OBJModel obj, ResourceLocation location) {
		this.obj = obj;
		this.location = location;
		this.material = obj.getTextures(configuration, a -> null, new HashSet<>()).iterator().next();
		this.name = location.getPath().replace("objmodels", "");
		this.textureLocation = new ResourceLocation(location.getNamespace(),
				"textures/" + material.getTextureLocation().getPath() + ".png");
	}

	public void bake() {
		IBakedModel bakedModel = obj.bake(configuration, null, this::getSprite, transform, overrideList, location);
		quads.addAll(bakedModel.getQuads(null, null, null, null));
		this.obj = null;

	}

	private TextureAtlasSprite getSprite(RenderMaterial rm) {
		if (sprite == null) {
			sprite = TAS.getSprite(textureLocation);
		}
		return sprite;
	}

	public final IModelConfiguration configuration = new IModelConfiguration() {

		@Override
		public IUnbakedModel getOwnerModel() {
			return null;
		}

		@Override
		public String getModelName() {
			return name;
		}

		@Override
		public boolean isTexturePresent(String name) {
			return true;
		}

		@Override
		public RenderMaterial resolveTexture(String name) {
			return material;
		}

		@Override
		public boolean isShadedInGui() {
			return false;
		}

		@Override
		public boolean useSmoothLighting() {
			return true;
		}

		@Override
		public ItemCameraTransforms getCameraTransforms() {
			return ItemCameraTransforms.DEFAULT;
		}

		@Override
		public IModelTransform getCombinedTransform() {
			return transform;
		}

		@Override
		public boolean isSideLit() {
			return false;
		}

	};

	@Override
	public boolean isAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean isGui3d() {
		return true;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return null;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData extraData) {
		return quads;
	}
}
