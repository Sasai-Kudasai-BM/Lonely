package net.skds.mixins.lonely;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.skds.lonely.util.extended.EmptyNonNullList;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

	@Final
	@Shadow
	//public NonNullList<ItemStack> mainInventory = NonNullList.withSize(0, ItemStack.EMPTY);
	public NonNullList<ItemStack> mainInventory = new EmptyNonNullList();
	@Final
	@Shadow
	public NonNullList<ItemStack> armorInventory = new EmptyNonNullList();
	@Final
	@Shadow
	public NonNullList<ItemStack> offHandInventory = new EmptyNonNullList();
	
	@Shadow
	private final List<NonNullList<ItemStack>> allInventories = new ArrayList<>();

	@Overwrite
	public static int getHotbarSize() {
		return 1;
	}

	@Overwrite
	public static boolean isHotbar(int index) {
		return index == 0;
	}

}
