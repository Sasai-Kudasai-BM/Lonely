package net.skds.lonely.network;

import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.skds.core.SKDSCore;
import net.skds.lonely.reg.ContProvider;

public class OpenEGuiPacket {

	UUID id;

	public OpenEGuiPacket(PlayerEntity playerEntity) {
		id = playerEntity.getUniqueID();
	}

	public OpenEGuiPacket(PacketBuffer buffer) {
		id = buffer.readUniqueId();
	}

	public void encoder(PacketBuffer buffer) {
		buffer.writeUniqueId(id);
	}

	public static OpenEGuiPacket decoder(PacketBuffer buffer) {
		return new OpenEGuiPacket(buffer);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {

		if (SKDSCore.SERVER != null) {
			ServerPlayerEntity player = SKDSCore.SERVER.getPlayerList().getPlayerByUUID(id);
			player.openContainer(ContProvider.PLAYER_INV);
			//player.openContainer = new EContainer(player);
			//player.openContainer.addListener(player);
		}

		context.get().setPacketHandled(true);
	}
}
