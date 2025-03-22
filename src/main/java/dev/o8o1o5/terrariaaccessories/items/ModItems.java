package dev.o8o1o5.terrariaaccessories.registry;

import dev.o8o1o5.terrariaaccessories.TerrariaAccessories;
import dev.o8o1o5.terrariaaccessories.items.*;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TerrariaAccessories.MODID);

    public static final DeferredItem<Item> GLASS_JAR = ITEMS.register("glass_jar",
            () -> new GlassJarItem(new Item.Properties()));

    public static final DeferredItem<Item> HERMES_BOOTS = ITEMS.register("hermes_boots",
            () -> new HermesBootsItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SAILFISH_BOOTS = ITEMS.register("sailfish_boots",
            () -> new SailfishBootsItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ROCKET_BOOTS = ITEMS.register("rocket_boots",
            () -> new RocketBootsItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> CLOUD_IN_A_JAR = ITEMS.register("cloud_in_a_jar",
            () -> new CloudInAJarItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
