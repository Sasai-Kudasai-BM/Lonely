package net.skds.mixins.lonely;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.skds.lonely.util.extended.ENNL;
import net.skds.lonely.util.imix.IMixPEI;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements IMixPEI {

	@Shadow
	public final NonNullList<ItemStack> mainInventory = ENNL.withSize(36, ItemStack.EMPTY, (PlayerInventory) (Object) this, 2);

	@Shadow
	@Final
	private List<NonNullList<ItemStack>> allInventories;

	@Override
	public List<NonNullList<ItemStack>> getAllInventories() {
		return allInventories;
	}

}
