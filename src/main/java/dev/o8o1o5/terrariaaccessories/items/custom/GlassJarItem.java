package dev.o8o1o5.terrariaaccessories.items;

import dev.o8o1o5.terrariaaccessories.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class GlassJarItem extends Item {
    public GlassJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);

        if (!level.isClientSide) {
            if (player.getY() >= 190 && player.getY() <= 195) {
                itemStack.shrink(1);

                ItemStack result = new ItemStack(ModItems.CLOUD_IN_A_JAR.get());
                if (!player.getInventory().add(result)) {
                    player.drop(result, false);
                }

                level.playSound(null, player.blockPosition(), SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
                player.swing(usedHand, true);

                return InteractionResultHolder.success(itemStack);
            }
        }

        return InteractionResultHolder.pass(itemStack);
    }
}
