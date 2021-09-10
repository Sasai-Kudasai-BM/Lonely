package net.skds.mixins.lonely;

import com.mojang.blaze3d.matrix.MatrixStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Slot;

@Mixin(ContainerScreen.class)
public interface ContainerScreenInvoker {

	@Invoker("moveItems")	
	public void moveItemsPublic(MatrixStack matrixStack, Slot slot);

}
