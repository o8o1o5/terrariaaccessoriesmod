package dev.o8o1o5.terrariaaccessories.registry;

import dev.o8o1o5.terrariaaccessories.TerrariaAccessories;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerrariaAccessories.MODID);

    public static final Supplier<CreativeModeTab> TERRARIA_ACCESSORIES = CREATIVE_MODE_TAB.register("terraria_accessories_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.HERMES_BOOTS.get()))
                    .title(Component.translatable("itemGroup.terrariaaccessories.terraria_accessories"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GLASS_JAR);
                        output.accept(ModItems.HERMES_BOOTS);
                        output.accept(ModItems.CLOUD_IN_A_JAR);
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
