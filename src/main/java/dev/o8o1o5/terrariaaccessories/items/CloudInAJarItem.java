package dev.o8o1o5.terrariaaccessories.items;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudInAJarItem extends Item implements ICurioItem {

    private static final Map<UUID, Boolean> canDoubleJump = new HashMap<>();
    private static final Map<UUID, Boolean> jumpKeyWasDown = new HashMap<>();
    private static final Map<UUID, Integer> ticksInAir = new HashMap<>();

    public CloudInAJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) return;
        UUID uuid = player.getUUID();

        if (!player.level().isClientSide) {
            if (player.onGround()) {
                canDoubleJump.put(uuid, true);
                ticksInAir.put(uuid, 0);
            }
            return;
        }

        if (!player.equals(Minecraft.getInstance().player)) return;

        if (!player.onGround()) {
            ticksInAir.put(uuid, ticksInAir.getOrDefault(uuid, 0) + 1);
        } else {
            ticksInAir.put(uuid, 0);
        }

        boolean jumpNow = Minecraft.getInstance().options.keyJump.isDown();
        boolean jumpBefore = jumpKeyWasDown.getOrDefault(uuid, false);
        boolean justPressed = jumpNow && !jumpBefore;
        int airTicks = ticksInAir.getOrDefault(uuid, 0);

        if (justPressed && airTicks > 1 && canDoubleJump.getOrDefault(uuid, false)) {
            player.setDeltaMovement(player.getDeltaMovement().x, 0.5, player.getDeltaMovement().z);
            player.hasImpulse = true;
            player.level().playSound(player, player.blockPosition(), SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

            canDoubleJump.put(uuid, false);
        }

        jumpKeyWasDown.put(uuid, jumpNow);
    }
}
