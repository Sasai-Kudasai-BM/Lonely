package net.skds.lonely.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.util.other.collision.OBBShape;
import net.skds.lonely.client.inventory.ClickOBBShape;
import net.skds.lonely.client.inventory.BodyPart.Segment;
import net.skds.lonely.item.ILonelyEquipItem;

public class EPlayerInventory extends PlayerInventory {

	public static final int eqOffset = 2;

	public final NonNullList<ItemStack> equipmentSlots = NonNullList.withSize(EquipmentLayer.values().length,
			ItemStack.EMPTY);

	public EPlayerInventory(PlayerEntity playerIn) {
		super(playerIn);
	}

	public boolean isLonely() {
		return !player.abilities.isCreativeMode;
	}

	public boolean canEquip(ItemStack stack, EquipmentLayer layer) {
		if (stack.isEmpty()) {
			return false;
		}
		if (!layer.canInteract(equipmentSlots)) {
			return false;
		}
		if (stack.getItem() instanceof ILonelyEquipItem) {
			ILonelyEquipItem it = (ILonelyEquipItem) stack.getItem();
			if (!it.getLayers(stack).contains(layer)) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean canUnequip(EquipmentLayer layer) {
		if (!layer.canInteract(equipmentSlots)) {
			return false;
		}
		return true;
	}

	public void equip(ItemStack stack, EquipmentLayer layer) {
		setInventorySlotContents(layer.ordinal() + eqOffset, stack);
	}

	public boolean tryEquip(ItemStack stack, EquipmentLayer layer) {
		if (canEquip(stack, layer)) {
			equip(stack, layer);
			return true;
		}
		return false;
	}

	public ItemStack unequip(EquipmentLayer layer) {
		ItemStack stack = getStackInSlot(layer.ordinal() + eqOffset);
		setInventorySlotContents(layer.ordinal() + eqOffset, ItemStack.EMPTY);
		return stack;
	}

	public ItemStack getEquipmentOnLayer(EquipmentLayer layer) {
		return getStackInSlot(layer.ordinal() + eqOffset);
	}

	public boolean clickLayer(EquipmentLayer layer) {
		ItemStack mouse = getItemStack();
		ItemStack stack = getEquipmentOnLayer(layer);
		if (mouse.isEmpty() && stack.isEmpty()) {
			return false;
		}
		if (!layer.canInteract(equipmentSlots)) {
			return false;
		}
		if (!mouse.isEmpty()) {
			if (!canEquip(mouse, layer)) {
				return false;
			}
		}
		ItemStack newMouse = ItemStack.EMPTY;
		if (!stack.isEmpty()) {
			if (canUnequip(layer)) {
				newMouse = unequip(layer);
			} else {
				return false;
			}
		}
		equip(mouse, layer);
		setItemStack(newMouse);

		return true;
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

	@OnlyIn(Dist.CLIENT)
	public List<ClickOBBShape> getClickShapes() {
		List<ClickOBBShape> out = new ArrayList<>();
		for (int i = 0; i < this.equipmentSlots.size(); ++i) {
			ItemStack stack = equipmentSlots.get(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ILonelyEquipItem) {
				ILonelyEquipItem iEquipItem = ((ILonelyEquipItem) stack.getItem());
				for (EquipmentLayer layer : iEquipItem.getLayers(stack)) {
					Map<Segment, OBBShape> shapes = iEquipItem.getShapeMap(stack, layer);
					for (Map.Entry<Segment, OBBShape> e : shapes.entrySet()) {
						ClickOBBShape clickShape = new ClickOBBShape(e.getValue().getBoxes(), EquipmentLayer.get(i),
								stack, e.getKey());
						out.add(clickShape);
					}
				}
			}
		}
		return out;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (isLonely() && index >= eqOffset && index < eqOffset + EquipmentLayer.values().length) {
			return equipmentSlots.get(index - eqOffset);
		}
		return super.getStackInSlot(index);
	}

	private int storePartialItemStack(ItemStack itemStackIn) {
		int i = this.storeItemStack(itemStackIn);
		if (i == -1) {
			i = this.getFirstEmptyStack();
		}
		//System.out.println(i);
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

		if (isLonely() && index >= eqOffset && index < eqOffset + EquipmentLayer.values().length) {
			equipmentSlots.set(index - eqOffset, stack);
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
					compoundnbt.putByte("Slot", (byte) (i + eqOffset));
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
				if (j >= eqOffset && j < eqOffset + EquipmentLayer.values().length) {
					ItemStack itemstack = ItemStack.read(compoundnbt);
					if (!itemstack.isEmpty()) {
						equipmentSlots.set(j - eqOffset, itemstack);
					}
				}
			}
		}
		super.read(nbtTagListIn);
	}
}
