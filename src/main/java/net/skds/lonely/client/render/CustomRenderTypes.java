package net.skds.lonely.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class CustomRenderTypes extends RenderType {

	public static final RenderType HIGHLIGHT = getHL();

	public CustomRenderTypes(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn,
			boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType getHL() {
		RenderType.State state = RenderType.State.getBuilder().transparency(ADDITIVE_TRANSPARENCY).depthTest(DEPTH_LEQUAL).diffuseLighting(DIFFUSE_LIGHTING_DISABLED).build(false);
		return makeType("highlight", DefaultVertexFormats.POSITION_COLOR, 7, 256, false, true, state);
	}
}
