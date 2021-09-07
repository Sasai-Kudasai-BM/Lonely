package net.skds.lonely;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skds.core.network.PacketHandler;
import net.skds.lonely.inventory.EContainer;
import net.skds.lonely.inventory.EGui;
import net.skds.lonely.network.OpenEGuiPacket;
import net.skds.lonely.util.imix.IMixPE;

public class Events {
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent e) {
	}

	@SubscribeEvent
	public void itemPickupEvent(ItemPickupEvent e) {
		if (!e.getPlayer().abilities.isCreativeMode) {

		}
	}

	@SubscribeEvent
	public void playerTicdk(GuiOpenEvent e) {
		if (e.getGui() != null && e.getGui() instanceof InventoryScreen) {
			//e.getGui().
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			if (player != null && !player.abilities.isCreativeMode) {
				EContainer container = ((IMixPE) player).getCont();
				player.openContainer = container;
				EGui gui = new EGui(container, player.inventory);
				e.setGui(gui);
				PacketHandler.sendToServer(new OpenEGuiPacket(player));
			}
		}
	}
}
