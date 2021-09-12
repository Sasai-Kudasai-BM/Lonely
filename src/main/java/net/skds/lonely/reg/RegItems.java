package net.skds.lonely.reg;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.skds.lonely.Lonely;
import net.skds.lonely.item.ILonelyItem;
import net.skds.lonely.item.items.BackPackItem;

public class RegItems {
	
	public static final ItemGroup CTAB = (new ItemGroup(ItemGroup.getGroupCountSafe(), Lonely.MOD_NAME) {
    
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.LEVER);
        }
    }).setTabPath(Lonely.MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Lonely.MOD_ID);

	public static final RegistryObject<BackPackItem> BACKPACK = register(new BackPackItem());
	
	public static <T extends Item & ILonelyItem> RegistryObject<T> register(T item) {
		return RegItems.ITEMS.register(item.getId(), () -> item);
	}

	public static void reg(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
