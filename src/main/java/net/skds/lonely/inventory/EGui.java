package net.skds.lonely.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TranslationTextComponent;

public class EGui extends DisplayEffectsScreen<EContainer> {
	/** The old x position of the mouse pointer */
	private float oldMouseX;
	/** The old y position of the mouse pointer */
	private float oldMouseY;
	private boolean buttonClicked;

	public EGui(EContainer screenContainer, PlayerInventory inv) {
		super(screenContainer, inv, new TranslationTextComponent("lonely.jopa"));
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void init() {
		super.init();
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		//this.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
		this.oldMouseX = (float)mouseX;
		this.oldMouseY = (float)mouseY;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
		drawEntityOnScreen(i + 51, j + 75, 30, (float) (i + 51) - this.oldMouseX,
				(float) (j + 75 - 50) - this.oldMouseY, this.minecraft.player, partialTicks);

	}

	@Override
	public void renderBackground(MatrixStack matrixStack) {
		super.renderBackground(matrixStack);
	}

	@Override
	protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
		//System.out.println(hoveredSlot);
		super.renderHoveredTooltip(matrixStack, x, y);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		this.font.func_243248_b(matrixStack, this.title, (float) this.titleX + 100, (float) this.titleY, 4210752);
	}

	@SuppressWarnings("deprecation")
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY,
			LivingEntity livingEntity, float partialTicks) {
		float f = (float) Math.atan((double) (mouseX / 40.0F));
		float f1 = (float) Math.atan((double) (mouseY / 40.0F));
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float) scale, (float) scale, (float) scale);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		//Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
		Quaternion quaternion1 = Vector3f.YP.rotationDegrees(((float) livingEntity.ticksExisted + partialTicks) * 20);
		quaternion.multiply(quaternion1);
		matrixstack.rotate(quaternion);
		float f2 = livingEntity.renderYawOffset;
		float f3 = livingEntity.rotationYaw;
		float f4 = livingEntity.rotationPitch;
		float f5 = livingEntity.prevRotationYawHead;
		float f6 = livingEntity.rotationYawHead;
		livingEntity.renderYawOffset = 180.0F + f * 20.0F;
		livingEntity.rotationYaw = 180.0F + f * 40.0F;
		livingEntity.rotationPitch = -f1 * 20.0F;
		livingEntity.rotationYawHead = livingEntity.rotationYaw;
		livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		quaternion1.conjugate();
		entityrenderermanager.setCameraOrientation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers()
				.getBufferSource();
		RenderSystem.runAsFancy(() -> {
			entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack,
					irendertypebuffer$impl, 15728880);
		});
		irendertypebuffer$impl.finish();
		entityrenderermanager.setRenderShadow(true);
		livingEntity.renderYawOffset = f2;
		livingEntity.rotationYaw = f3;
		livingEntity.rotationPitch = f4;
		livingEntity.prevRotationYawHead = f5;
		livingEntity.rotationYawHead = f6;
		RenderSystem.popMatrix();
	}

	@Override
	protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
		return super.isPointInRegion(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (this.buttonClicked) {
			this.buttonClicked = false;
			return true;
		} else {
			return super.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Override
	protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
		//System.out.println(mouseX + " " + mouseY);
		return super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
		//return false;
	}

	/**
	 * Called when the mouse is clicked over a slot or outside the gui.
	 */
	@Override
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
		super.handleMouseClick(slotIn, slotId, mouseButton, type);
		//System.out.println(slotId);
	}

	@Override
	public void onClose() {
		super.onClose();
	}
}
