package net.skds.lonely.util.extended;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.NonNullList;

public class ENNL<T> extends NonNullList<T> {
	public int size = 0;
	final PlayerInventory pi;

	@SuppressWarnings("unchecked")
	public static <E> NonNullList<E> withSize(int size, E fill, PlayerInventory pi, int fakeSize) {
		//Validate.notNull(fill);
		Object[] aobject = new Object[size];
		Arrays.fill(aobject, fill);
		ENNL<E> ennl = new ENNL<>(Arrays.asList((E[]) aobject), fill, pi);
		ennl.size = fakeSize;
		return ennl;
	}

	protected ENNL(List<T> delegateIn, T listType, PlayerInventory pi) {
		super(delegateIn, listType);
		this.pi = pi;
	}

	@Override
	public int size() {
		if (!pi.player.abilities.isCreativeMode) {
			return size;
		}
		return super.size();
	}
}
