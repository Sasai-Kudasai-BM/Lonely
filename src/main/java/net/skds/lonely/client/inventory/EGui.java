package net.skds.lonely.client.inventory;

import java.util.Iterator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import org.lwjgl.opengl.GL11C;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.skds.lonely.Lonely;
import net.skds.lonely.inventory.EContainer;
import net.skds.mixins.lonely.ContainerScreenInvoker;

@SuppressWarnings("deprecation")
public class EGui extends ContainerScreen<EContainer> {

	private final static ResourceLocation TEXTURE = new ResourceLocation(Lonely.MOD_ID, "textures/gui/inventory.png");

	private float oldMouseX = 0;
	private float oldMouseY = 0;
	private float bodyYaw = 0;
	private float bodyPitch = 0;
	private float prevBodyYaw = 0;
	private float prevBodyPitch = 0;
	private float bodyYawV = 0;
	private float bodyPitchV = 0;

	int clickBody = -1;

	private Minecraft mc = Minecraft.getInstance();

	private long[] mbPressedTime = new long[6];

	private int winX0;
	private int winY0;
	private int winX1;
	private int winY1;

	public EGui(EContainer screenContainer, PlayerInventory inv, ITextComponent tc) {
		super(screenContainer, inv, tc);
		xSize = 296;
		ySize = 166;

		winX0 = 100;
		winY0 = 4;
		winX1 = 93;
		winY1 = ySize - 8;
	}

	@Override
	public void tick() {

		prevBodyPitch = bodyPitch;
		prevBodyYaw = bodyYaw;

		bodyPitch += bodyPitchV;
		bodyYaw += bodyYawV;

		bodyPitch = Math.max(bodyPitch, -75);
		bodyPitch = Math.min(bodyPitch, 45);

		final float dec = 0.8F;
		bodyPitchV *= dec;
		bodyYawV *= dec;
		
		super.tick();
	}

	@Override
	protected void init() {
		super.init();
		this.guiLeft = (this.width - this.xSize) / 2;
		//this.guiTop = (this.height - this.ySize) / 2;
		//System.out.printf("%sw %sh ", width, height);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		partialTicks = Minecraft.getInstance().getRenderPartialTicks();
		rotateBody(matrixStack, mouseX, mouseY);
		RenderSystem.pushMatrix();

		RenderSystem.enableDepthTest();
		RenderSystem.translatef(0.0F, 0.0F, 1000.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.translatef(0.0F, 0.0F, -1000.0F);
		RenderSystem.depthFunc(GL11C.GL_GEQUAL);
		fill(matrixStack, guiLeft + winX0, guiTop + winY0, guiLeft + winX0 + winX1, guiTop + winY0 + winY1, -16777216);
		RenderSystem.depthFunc(GL11C.GL_LEQUAL);

		RenderSystem.translatef(0.0F, 0.0F, 200.0F);
		RenderSystem.disableDepthTest();

		renderBlack(matrixStack);
		renderGuiTexture(matrixStack, partialTicks);
		renderBody(partialTicks, mouseX, mouseY);

		RenderSystem.enableDepthTest();
		RenderSystem.depthFunc(GL11C.GL_GEQUAL);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.translatef(0.0F, 0.0F, 950.0F);
		RenderSystem.depthFunc(GL11C.GL_LEQUAL);
		RenderSystem.disableDepthTest();

		renderButtons(matrixStack, mouseX, mouseY, partialTicks);
		renderHands(matrixStack, mouseX, mouseY, partialTicks);
		renderItemOnCursor(playerInventory.getItemStack(), mouseX, mouseY);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		this.oldMouseX = (float) mc.mouseHelper.getMouseX();
		this.oldMouseY = (float) mc.mouseHelper.getMouseY();


		RenderSystem.popMatrix();
		//RenderSystem.fog(2918, 1.0F, 1.0F, 1.0F, 1.0F);
		//RenderSystem.fogStart(0.0F);
		//RenderSystem.fogEnd(0.1F);
		//RenderSystem.fogDensity(100.0F);
		//RenderSystem.fogMode(FogMode.LINEAR);
		//RenderSystem.setupNvFogDistance();
	}

	public void renderBlack(MatrixStack matrixStack) {
		this.fillGradient(matrixStack, 0, 0, this.width, this.height, -1072689136, -804253680);
	}

	protected void renderGuiTexture(MatrixStack matrixStack, float partialTicks) {
		matrixStack.push();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(TEXTURE);
		matrixStack.translate(guiLeft, guiTop, 0);
		matrixStack.scale(2, 2, 1);
		this.blit(matrixStack, 0, 0, 0, 0, this.xSize / 2, this.ySize / 2);
		matrixStack.pop();
	}

	protected void rotateBody(MatrixStack matrixStack, int mouseXi, int mouseYi) {
		float mouseX = (float) mc.mouseHelper.getMouseX();
		float mouseY = (float) mc.mouseHelper.getMouseY();
		boolean inZone = super.isPointInRegion(winX0, winY0, winX1, winY1, mouseXi, mouseYi);
		final float mul = -1.0F / mc.gameSettings.guiScale;
		if (inZone && isButtonPressed(0)) {
			float dx = (mouseX - oldMouseX) * mul;
			float dy = (mouseY - oldMouseY) * mul * 0.6F;
			bodyPitchV += dy;
			bodyYawV += dx;
		}

		if (Math.abs(bodyPitchV) < .1F) {
			bodyPitchV = 0;
		}
		if (Math.abs(bodyYawV) < .1F) {
			bodyYawV = 0;
		}
	}

	protected void renderBody(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.pushMatrix();

		final float scale = 60;
		int x0 = this.guiLeft + (xSize / 2);
		int y0 = this.guiTop + ySize - 20;
		RenderSystem.translatef(x0, y0, 10.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		PlayerEntity player = playerInventory.player;
		MatrixStack matrixStack2 = new MatrixStack();
		matrixStack2.scale(scale, scale, scale);

		final float offset = 0.5F;

		matrixStack2.translate(0, -offset, 0);
		Quaternion quaternion2 = new Quaternion(Vector3f.XN, MathHelper.lerp(partialTicks, prevBodyPitch, bodyPitch),
				true);
		quaternion2.multiply(new Quaternion(Vector3f.YP, MathHelper.lerp(partialTicks, prevBodyYaw, bodyYaw), true));
		matrixStack2.rotate(quaternion2);
		matrixStack2.translate(0, offset, 0);

		renderAmbient(partialTicks, matrixStack2, offset);

		Quaternion quaternion = new Quaternion(Vector3f.ZP, 180, true);
		quaternion.multiply(new Quaternion(Vector3f.YP, player.getYaw(partialTicks) + 180, true));
		
		matrixStack2.rotate(quaternion);
		

		//System.out.println(v);
		//Quat pp = new Quat(Vec3.XP, 30, true);
		//OBB obb = new OBB(prikol.offset(prikol.getCenter().scale(-1)), new Matrix3(pp), new Vec3(prikol.getCenter()));

		//IVertexBuilder buffer = mc.getRenderTypeBuffers().getBufferSource().getBuffer(RenderType.LINES);
		//WorldRenderer.drawBoundingBox(matrixStack2, buffer, new AxisAlignedBB(-0.25, 0.75, -0.15, 0.25, 1.3, -0.4), 0, 1, 0, 1);

		//========================================================
		//obb.render(matrixStack2.getLast().getMatrix(), buffer, 1, 1, 0, 1);
		//========================================================
		
		if (clickBody >= 0) {
			clickBodyPlace(matrixStack2, mouseX, mouseY, clickBody);
			clickBody = -1;
		}

		EntityRendererManager entityrenderermanager = mc.getRenderManager();
		EntityRenderer<? super PlayerEntity> pr = entityrenderermanager.getRenderer(player);
		IRenderTypeBuffer.Impl buffer2 = mc.getRenderTypeBuffers().getBufferSource();
		entityrenderermanager.setRenderShadow(false);
		pr.render(player, 0, partialTicks, matrixStack2, buffer2, 15728880);

		buffer2.finish();
		entityrenderermanager.setRenderShadow(true);
		RenderSystem.popMatrix();
	}

	protected void renderAmbient(float partialTicks, MatrixStack matrixStack, float offset) {
		RenderSystem.pushMatrix();
		matrixStack.push();
		PlayerEntity player = playerInventory.player;

		matrixStack.rotate(new Quaternion(Vector3f.ZP, 180, true));
		matrixStack.rotate(new Quaternion(Vector3f.YN, 180 - player.getYaw(partialTicks), true));

		World world = player.world;
		Vector3d pos = player.getEyePosition(partialTicks).subtract(0, player.getEyeHeight(), 0);
		float d = 2.0F;
		AxisAlignedBB box = new AxisAlignedBB(pos.x - d, pos.y - (d / 2), pos.z - d, pos.x + d, pos.y + d, pos.z + d);
		Vector3d vec = pos.scale(-1);

		Iterator<VoxelShape> shapes = world.getCollisionShapes(player, box).iterator();
		IVertexBuilder buffer = mc.getRenderTypeBuffers().getBufferSource().getBuffer(RenderType.LINES);
		while (shapes.hasNext()) {
			VoxelShape vs = shapes.next();
			for (AxisAlignedBB aabb : vs.toBoundingBoxList()) {
				WorldRenderer.drawBoundingBox(matrixStack, buffer, aabb.offset(vec), 1, 0, 1, 1);
			}
		}

		IRenderTypeBuffer.Impl buffer2 = mc.getRenderTypeBuffers().getBufferSource();
		for (Entity entity : world.getEntitiesInAABBexcluding(player, box, e -> (e instanceof ItemEntity))) {
			matrixStack.push();
			Vector3d v3 = entity.getPositionVec().subtract(pos);
			matrixStack.translate(v3.x, v3.y, v3.z);
			EntityRendererManager entityrenderermanager = mc.getRenderManager();
			EntityRenderer<? super Entity> ir = entityrenderermanager.getRenderer(entity);
			ir.render(entity, 0, partialTicks, matrixStack, buffer2, 15728880);
			matrixStack.pop();
		}

		matrixStack.pop();
		RenderSystem.popMatrix();
	}

	protected void renderButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < this.buttons.size(); ++i) {
			this.buttons.get(i).render(matrixStack, mouseX, mouseY, partialTicks);
		}
	}

	protected void renderHands(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.hoveredSlot = null;
		for (int i = 0; i < this.container.inventorySlots.size(); ++i) {
			Slot slot = this.container.inventorySlots.get(i);
			RenderSystem.pushMatrix();
			float dx = guiLeft;
			float dy = guiTop;
			RenderSystem.translatef(dx, dy, 0);

			if (isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY)) {
				this.hoveredSlot = slot;
				RenderSystem.disableDepthTest();
				RenderSystem.colorMask(true, true, true, false);
				int slotColor = this.getSlotColor(i);
				this.fillGradient(matrixStack, slot.xPos, slot.yPos, slot.xPos + 32, slot.yPos + 32, slotColor,
						slotColor);
				RenderSystem.colorMask(true, true, true, true);
				RenderSystem.enableDepthTest();
			}

			if (!playerInventory.getItemStack().isEmpty()) {
				RenderSystem.pushMatrix();
				RenderSystem.translatef(-slot.xPos, -slot.yPos, 0);
				RenderSystem.scalef(2, 2, 1);
				((ContainerScreenInvoker) this).moveItemsPublic(matrixStack, slot);
				RenderSystem.popMatrix();
			} else {

				ItemStack stack = slot.getStack();
				RenderSystem.translatef(slot.xPos, slot.yPos, -100);
				RenderSystem.scalef(2, 2, 1);
				renderItemStack(stack, 0, 0, null);
			}
			RenderSystem.popMatrix();
		}
	}

	protected void renderItemStack(ItemStack stack, int x, int y, String altText) {
		this.setBlitOffset(200);
		this.itemRenderer.zLevel = 200.0F;
		FontRenderer font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = this.font;
		this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
		this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y, altText);
		this.setBlitOffset(0);
		this.itemRenderer.zLevel = 0.0F;
	}

	protected void renderItemOnCursor(ItemStack stack, int mouseX, int mouseY) {
		if (stack.isEmpty()) {
			return;
		}
		final float scale = 1.5F;
		RenderSystem.pushMatrix();
		RenderSystem.translatef(mouseX - 8, mouseY - 8, 1);
		RenderSystem.scalef(scale, scale, 1);
		renderItemStack(stack, 0, 0, null);
		RenderSystem.popMatrix();
	}

	@Override
	protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
		if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null
				&& this.hoveredSlot.getHasStack()) {
			this.renderTooltip(matrixStack, this.hoveredSlot.getStack(), x, y);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		this.font.func_243248_b(matrixStack, this.title, (float) this.titleX + 100, (float) this.titleY, 4210752);
	}

	@Override
	protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
		return super.isPointInRegion(x, y, width + 16, height + 16, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button < mbPressedTime.length) {
			mbPressedTime[button] = System.currentTimeMillis() - 1;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (button < mbPressedTime.length) {
			if (getPressedTime(button) < 200) {
				if (super.isPointInRegion(winX0, winY0, winX1, winY1, mouseX, mouseY)) {
					clickBody = button;
				}
			}

			mbPressedTime[button] = -1;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}

	private void clickBodyPlace(MatrixStack matrixStack, int mouseX, int mouseY, int button) {
		matrixStack.push();
		matrixStack.rotate(new Quaternion(Vector3f.YN, playerInventory.player.renderYawOffset, true));

		float x0 = mouseX;
		float y0 = mouseY;
		x0 -= this.guiLeft + (xSize / 2);
		y0 -= this.guiTop + ySize - 20;

		x0 /= 60;
		y0 /= 60;
		Matrix3f matrix3f = matrixStack.getLast().getNormal();
		matrix3f.transpose();
		Matrix4f matrix4f = matrixStack.getLast().getMatrix();

		TransformationMatrix transformationMatrix = new TransformationMatrix(matrix4f);

		Vector3f trans = transformationMatrix.getTranslation();
		trans.mul(1F / 60);

		Vector3f vf1 = new Vector3f(x0, y0, 100);
		Vector3f vf2 = new Vector3f(x0, y0, -100);
		vf1.sub(trans);
		vf2.sub(trans);
		vf1.transform(matrix3f);
		vf2.transform(matrix3f);

		
		//Quat pp = new Quat(Vec3.XP, 30, true);
		//OBB obb = new OBB(prikol.offset(prikol.getCenter().scale(-1)), new Matrix3(pp), new Vec3(prikol.getCenter()));

		//Optional<Vector3d> op = obb.rayTrace(vf1, vf2);
		//Optional<Vector3d> op = prikol.rayTrace(new Vector3d(vf1), new Vector3d(vf2));
		//if (op.isPresent()) {
		//	Vector3d rt = op.get();
		//	System.out.println(rt);
		//}

		matrixStack.pop();
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
		return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
	}

	/**
	 * Called when the mouse is clicked over a slot or outside the gui.
	 */
	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
	}

	private boolean isButtonPressed(int i) {
		return getPressedTime(i) > 0;
	}

	private int getPressedTime(int i) {
		if (i < mbPressedTime.length && mbPressedTime[i] > 0) {
			return (int) (System.currentTimeMillis() - mbPressedTime[i]);
		}
		return 0;
	}
}
