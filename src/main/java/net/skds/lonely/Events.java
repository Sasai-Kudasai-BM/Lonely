package net.skds.lonely;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.skds.core.network.PacketHandler;
import net.skds.lonely.entity.LonelyItemEntity;
import net.skds.lonely.network.OpenEGuiPacket;

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
				e.setCanceled(true);
				PacketHandler.sendToServer(new OpenEGuiPacket(player));
			}
		}
	}

	@SubscribeEvent
	public void entitySpawn(EntityJoinWorldEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof ItemEntity) {
			ItemEntity itemEntity = (ItemEntity) entity;
			e.setCanceled(true);
			LonelyItemEntity lItemEntity = new LonelyItemEntity(itemEntity);
			e.getWorld().addEntity(lItemEntity);
		}
	}
}
