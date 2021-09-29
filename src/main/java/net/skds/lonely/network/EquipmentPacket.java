package net.skds.lonely.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.skds.lonely.inventory.EPlayerInventory;
import net.skds.lonely.inventory.EquipmentLayer;

public class EquipmentPacket {

	//ItemStack mouseStack;
	//ItemStack layerStack;
	EquipmentLayer layer;

	//public EquipmentPacket(ItemStack mouseStack, ItemStack layerStack, EquipmentLayer layer) {
	public EquipmentPacket(EquipmentLayer layer) {
		this.layer = layer;
		//this.layerStack = layerStack;
		//this.mouseStack = mouseStack;
	}

	public EquipmentPacket(PacketBuffer buffer) {
		layer = buffer.readEnumValue(EquipmentLayer.class);
		//mouseStack = buffer.readItemStack();
		//layerStack = buffer.readItemStack();
	}

	public void encoder(PacketBuffer buffer) {
		buffer.writeEnumValue(layer);
		//buffer.writeItemStack(mouseStack);
		//buffer.writeItemStack(layerStack);
	}

	public static EquipmentPacket decoder(PacketBuffer buffer) {
		return new EquipmentPacket(buffer);
	}

	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().setPacketHandled(true);
		//if (layerStack.isEmpty() && mouseStack.isEmpty()) {
		//	return;
		//}
		EPlayerInventory inventory = (EPlayerInventory) context.get().getSender().inventory;

		inventory.clickLayer(layer);
	}
}
