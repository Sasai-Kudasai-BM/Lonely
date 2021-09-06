package net.skds.lonely.util.extended;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class EmptyNonNullList extends NonNullList<ItemStack> {
	
	public EmptyNonNullList() {
		super(new ArrayList<ItemStack>(), ItemStack.EMPTY);
	}

	@Override
	public ItemStack get(int i) {
		return ItemStack.EMPTY;
	}
 
	@Override
	public ItemStack set(int a, ItemStack b) {
	   return ItemStack.EMPTY;
	}
 
	@Override
	public void add(int a, ItemStack b) {}
 
	@Override
	public ItemStack remove(int i) {
	   return ItemStack.EMPTY;
	}
 
	@Override
	public int size() {
	   return 0;
	}
 
	@Override
	public void clear() {
	}
}
