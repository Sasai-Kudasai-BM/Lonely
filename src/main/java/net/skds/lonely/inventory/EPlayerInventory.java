package net.skds.lonely.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class EPlayerInventory extends PlayerInventory {

	public final NonNullList<ItemStack> equipmentSlots = NonNullList.withSize(EquipmentLayer.values().length,
			ItemStack.EMPTY);

	public EPlayerInventory(PlayerEntity playerIn) {
		super(playerIn);
	}

	public boolean isLonely() {
		return !player.abilities.isCreativeMode;
	}

	@Override
	public int getSizeInventory() {
		if (isLonely()) {
			return 2;
		}
		return super.getSizeInventory();
	}

	@Override
	public int getBestHotbarSlot() {
		if (isLonely()) {
			return 0;
		}
		return super.getBestHotbarSlot();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (isLonely() && index >= 10 && index < 10 + EquipmentLayer.values().length) {
			return equipmentSlots.get(index - 10);
		}
		return super.getStackInSlot(index);
	}

	private int storePartialItemStack(ItemStack itemStackIn) {
		int i = this.storeItemStack(itemStackIn);
		if (i == -1) {
			i = this.getFirstEmptyStack();
		}
		System.out.println(i);
		return i == -1 ? itemStackIn.getCount() : this.addResource(i, itemStackIn);
	}

	private int addResource(int ind, ItemStack stack) {
		//Item item = stack.getItem();
		int i = stack.getCount();
		ItemStack itemstack = this.getStackInSlot(ind);
		if (itemstack.isEmpty()) {
			itemstack = stack.copy(); // Forge: Replace Item clone above to preserve item capabilities when picking the item up.
			itemstack.setCount(0);
			if (stack.hasTag()) {
				itemstack.setTag(stack.getTag().copy());
			}

			this.setInventorySlotContents(ind, itemstack);
		}

		int j = i;
		if (i > itemstack.getMaxStackSize() - itemstack.getCount()) {
			j = itemstack.getMaxStackSize() - itemstack.getCount();
		}

		if (j > this.getInventoryStackLimit() - itemstack.getCount()) {
			j = this.getInventoryStackLimit() - itemstack.getCount();
		}

		if (j == 0) {
			return i;
		} else {
			i = i - j;
			itemstack.grow(j);
			itemstack.setAnimationsToGo(5);
			return i;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (player.world.isRemote) {
			//System.out.println(stack);
		}
		
		if (isLonely() && index >= 10 && index < 10 + EquipmentLayer.values().length) {
			equipmentSlots.set(index - 10, stack);
			return;
		}
		if (isLonely() && !(index >= -1 && index < 2)) {
			if (stack.getItem() == Items.BARRIER) {
				return;
			}
			storePartialItemStack(stack);
		}
		super.setInventorySlotContents(index, stack);
	}

	@Override
	public void pickItem(int index) {
		super.pickItem(index);
	}

	@Override
	public void placeItemBackInInventory(World worldIn, ItemStack stack) {
		super.placeItemBackInInventory(worldIn, stack);
	}

	@Override
	public void setPickedItemStack(ItemStack stack) {
		super.setPickedItemStack(stack);
	}

	@Override
	public int getFirstEmptyStack() {
		return super.getFirstEmptyStack();
	}

	@Override
	public ItemStack getCurrentItem() {
		if (isLonely()) {
			return getStackInSlot(0);
		}
		return super.getCurrentItem();
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public void changeCurrentItem(double direction) {
		if (isLonely()) {
			currentItem = 0;
			return;
		}
		super.changeCurrentItem(direction);
	}

	@Override
	public int storeItemStack(ItemStack itemStackIn) {
		return super.storeItemStack(itemStackIn);
	}

	@Override
	public ListNBT write(ListNBT nbtTagListIn) {
		if (isLonely()) {
			for (int i = 0; i < this.equipmentSlots.size(); ++i) {
				if (!this.equipmentSlots.get(i).isEmpty()) {
					CompoundNBT compoundnbt = new CompoundNBT();
					compoundnbt.putByte("Slot", (byte) (i + 10));
					this.equipmentSlots.get(i).write(compoundnbt);
					nbtTagListIn.add(compoundnbt);
				}
			}
		}
		return super.write(nbtTagListIn);
	}

	@Override
	public void read(ListNBT nbtTagListIn) {
		if (isLonely()) {
			equipmentSlots.clear();

			for (int i = 0; i < nbtTagListIn.size(); ++i) {
				CompoundNBT compoundnbt = nbtTagListIn.getCompound(i);
				int j = compoundnbt.getByte("Slot") & 255;
				if (j >= 10 && j < 10 + EquipmentLayer.values().length) {
					ItemStack itemstack = ItemStack.read(compoundnbt);
					if (!itemstack.isEmpty()) {
						equipmentSlots.set(j - 10, itemstack);
					}
				}
			}
		}
		super.read(nbtTagListIn);
	}
}
