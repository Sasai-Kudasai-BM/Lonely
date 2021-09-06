package net.skds.lonely.util.extended;

import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EPlayerInventory extends PlayerInventory {

	public ItemStack mainHand = ItemStack.EMPTY;
	public ItemStack offHand = ItemStack.EMPTY;

	public ItemStack itemStack = ItemStack.EMPTY;

	public EPlayerInventory(PlayerEntity playerIn) {
		super(playerIn);
	}

	private boolean canMergeStacks(ItemStack stack1, ItemStack stack2) {
		return !stack1.isEmpty() && this.stackEqualExact(stack1, stack2) && stack1.isStackable()
				&& stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getInventoryStackLimit();
	}

	@Override
	public ItemStack getCurrentItem() {
		return mainHand;
	}

	@Override
	public int getFirstEmptyStack() {
		if (mainHand.isEmpty()) {
			return 0;
		} else if (offHand.isEmpty()) {
			return 1;
		}
		return -1;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPickedItemStack(ItemStack stack) {
		mainHand = stack;
	}

	@Override
	public void pickItem(int index) {
		//this.currentItem = this.getBestHotbarSlot();
		//ItemStack itemstack = offHand;
		//offHand = mainHand;
		//mainHand = itemstack;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public int getBestHotbarSlot() {
		return 0;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getSlotFor(ItemStack stack) {
		if (!mainHand.isEmpty() && stackEqualExact(stack, mainHand)) {
			return 0;
		} else if (!offHand.isEmpty() && stackEqualExact(stack, offHand)) {
			return 1;
		}
		return -1;
	}

	private boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	@Override
	public int findSlotMatchingUnusedItem(ItemStack stack) {

		if (!mainHand.isEmpty() && stackEqualExact(stack, mainHand) && !mainHand.isDamaged() && !mainHand.isEnchanted()
				&& !mainHand.hasDisplayName()) {
			return 0;
		} else if (!offHand.isEmpty() && stackEqualExact(stack, offHand) && !offHand.isDamaged()
				&& !offHand.isEnchanted() && !offHand.hasDisplayName()) {
			return 1;
		}
		return -1;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void changeCurrentItem(double direction) {
	}

	// clear
	@Override
	public int func_234564_a_(Predicate<ItemStack> predicate, int count, IInventory inventory) {
		int i = 0;
		if (!mainHand.isEmpty()) {
			if (predicate.test(mainHand)) {
				i += mainHand.getCount();
				mainHand = ItemStack.EMPTY;
			}
		}
		if (!offHand.isEmpty()) {
			if (predicate.test(offHand)) {
				i += offHand.getCount();
				offHand = ItemStack.EMPTY;
			}
		}

		if (!itemStack.isEmpty()) {
			if (predicate.test(itemStack)) {
				i += itemStack.getCount();
				itemStack = ItemStack.EMPTY;
			}
		}
		return i;
	}

	private int storePartialItemStack(ItemStack itemStackIn) {
		int i = this.storeItemStack(itemStackIn);
		if (i == -1) {
			i = this.getFirstEmptyStack();
		}

		return i == -1 ? itemStackIn.getCount() : this.addResource(i, itemStackIn);
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index == 0) {
			return mainHand;
		} else if (index == 1) {
			return offHand;
		}
		return ItemStack.EMPTY;
	}

	private int addResource(int slot, ItemStack stack) {
		int i = stack.getCount();
		ItemStack itemstack = this.getStackInSlot(slot);
		if (itemstack.isEmpty()) {
			itemstack = stack.copy(); // Forge: Replace Item clone above to preserve item capabilities when picking the item up.
			itemstack.setCount(0);
			if (stack.hasTag()) {
				itemstack.setTag(stack.getTag().copy());
			}

			this.setInventorySlotContents(slot, itemstack);
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
	public int storeItemStack(ItemStack itemStackIn) {
		if (this.canMergeStacks(mainHand, itemStackIn)) {
			return 0;
		} else if (this.canMergeStacks(offHand, itemStackIn)) {
			return 1;
		}
		return -1;
	}

	@Override
	public void tick() {
		mainHand.inventoryTick(this.player.world, this.player, 0, this.currentItem == 0);
		offHand.inventoryTick(this.player.world, this.player, 1, this.currentItem == 1);
	}

	@Override
	public boolean addItemStackToInventory(ItemStack itemStackIn) {
		return super.addItemStackToInventory(itemStackIn);
	}

	public void setSlot(int slot, ItemStack stack) {
		if (slot == 0) {
			mainHand = stack;
			mainHand.setAnimationsToGo(5);
		} else if (slot == 1) {
			offHand = stack;
			offHand.setAnimationsToGo(5);
		}
	}

	@Override
	public boolean add(int slotIn, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		} else {
			try {
				if (stack.isDamaged()) {
					if (slotIn == -1) {
						slotIn = this.getFirstEmptyStack();
					}

					if (slotIn >= 0) {
						setSlot(slotIn, stack);
						stack.setCount(0);
						return true;
					} else if (this.player.abilities.isCreativeMode) {
						stack.setCount(0);
						return true;
					} else {
						return false;
					}
				} else {
					int i;
					do {
						i = stack.getCount();
						if (slotIn == -1) {
							stack.setCount(this.storePartialItemStack(stack));
						} else {
							stack.setCount(this.addResource(slotIn, stack));
						}
					} while (!stack.isEmpty() && stack.getCount() < i);

					if (stack.getCount() == i && this.player.abilities.isCreativeMode) {
						stack.setCount(0);
						return true;
					} else {
						return stack.getCount() < i;
					}
				}
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
				crashreportcategory.addDetail("Registry Name", () -> String.valueOf(stack.getItem().getRegistryName()));
				crashreportcategory.addDetail("Item Class", () -> stack.getItem().getClass().getName());
				crashreportcategory.addDetail("Item ID", Item.getIdFromItem(stack.getItem()));
				crashreportcategory.addDetail("Item data", stack.getDamage());
				crashreportcategory.addDetail("Item name", () -> {
					return stack.getDisplayName().getString();
				});
				throw new ReportedException(crashreport);
			}
		}
	}

	@Override
	public void placeItemBackInInventory(World worldIn, ItemStack stack) {
		super.placeItemBackInInventory(worldIn, stack);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (index == 0 && !mainHand.isEmpty()) {
			return mainHand.split(count);
			//return mainHand;
		} else if (index == 1 && !offHand.isEmpty()) {
			return offHand.split(count);
			//return offHand;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void deleteStack(ItemStack stack) {
		if (stack == mainHand) {
			mainHand = ItemStack.EMPTY;
		}
		if (stack == offHand) {
			offHand = ItemStack.EMPTY;
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index == 0) {
			mainHand = ItemStack.EMPTY;
		} else if (index == 1) {
			offHand = ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.setSlot(index, stack);
	}

	@Override
	public float getDestroySpeed(BlockState state) {
		return mainHand.getDestroySpeed(state);
	}

	@Override
	public ListNBT write(ListNBT nbtTagListIn) {

		if (!mainHand.isEmpty()) {
			CompoundNBT compoundnbt = new CompoundNBT();
			compoundnbt.putByte("Slot", (byte) 0);
			mainHand.write(compoundnbt);
			nbtTagListIn.add(compoundnbt);
		}
		if (!offHand.isEmpty()) {
			CompoundNBT compoundnbt = new CompoundNBT();
			compoundnbt.putByte("Slot", (byte) 1);
			offHand.write(compoundnbt);
			nbtTagListIn.add(compoundnbt);
		}

		return nbtTagListIn;
	}

	@Override
	public void read(ListNBT nbtTagListIn) {

		clear();

		for (int i = 0; i < nbtTagListIn.size(); ++i) {
			CompoundNBT compoundnbt = nbtTagListIn.getCompound(i);
			int slot = compoundnbt.getByte("Slot") & 255;
			ItemStack itemstack = ItemStack.read(compoundnbt);
			if (!itemstack.isEmpty()) {
				setSlot(slot, itemstack);
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return mainHand.isEmpty() && offHand.isEmpty();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack armorItemInSlot(int slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void func_234563_a_(DamageSource p_234563_1_, float p_234563_2_) {
	}

	@Override
	public void dropAllItems() {
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty()) {
				this.player.dropItem(stack, true, false);
				deleteStack(stack);
			}
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getTimesChanged() {
		return super.getTimesChanged();
	}

	@Override
	public void setItemStack(ItemStack itemStackIn) {
		super.setItemStack(itemStackIn);
	}

	@Override
	public ItemStack getItemStack() {
		return super.getItemStack();
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return super.isUsableByPlayer(player);
	}

	@Override
	public boolean hasTag(ITag<Item> itemTag) {
		return super.hasTag(itemTag);
	}

	@Override
	public void clear() {
		mainHand = ItemStack.EMPTY;
		offHand = ItemStack.EMPTY;
		itemStack = ItemStack.EMPTY;
	}

	@Override
	public void accountStacks(RecipeItemHelper rh) {
		super.accountStacks(rh);
	}

}
