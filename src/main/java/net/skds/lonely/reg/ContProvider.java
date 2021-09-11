package net.skds.lonely.reg;

import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.TranslationTextComponent;
import net.skds.lonely.inventory.EContainer;

public class ContProvider {

	public static final INamedContainerProvider PLAYER_INV = new SimpleNamedContainerProvider((windowId, inv, player) -> new EContainer(ContTypes.INV_CONT.get(), windowId, player.inventory), new TranslationTextComponent("lonely.inventory"));
	

}
