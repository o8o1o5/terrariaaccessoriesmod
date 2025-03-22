package dev.o8o1o5.terrariaaccessories.items;

import dev.o8o1o5.terrariaaccessories.core.CurioManager;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RocketBootsItem extends Item implements ICurioItem {
    private static final Map<UUID, Integer> boostTicks = new HashMap<>();
    private static final Map<UUID, Integer> ticksInAir = new HashMap<>();
    private static final Map<UUID, Boolean> jumpKeyWasDown = new HashMap<>();
    private static final int MAX_BOOST_TICKS = 16; // 1.2초

    public RocketBootsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) return;
        UUID uuid = player.getUUID();

        if (!player.level().isClientSide) {
            if (player.onGround()) {
                CurioManager.usedRocket.put(uuid, false);
                boostTicks.put(uuid, 0);
                ticksInAir.put(uuid, 0);
            }
            return;
        }

        if (!player.equals(Minecraft.getInstance().player)) return;

        boolean jumpNow = Minecraft.getInstance().options.keyJump.isDown();
        boolean jumpBefore = jumpKeyWasDown.getOrDefault(uuid, false);
        boolean justPressed = jumpNow && !jumpBefore;
        int airTicks = ticksInAir.getOrDefault(uuid, 0);

        if (!player.onGround()) {
            ticksInAir.put(uuid, airTicks + 1);
        } else {
            ticksInAir.put(uuid, 0);
        }

        int currentBoost = boostTicks.getOrDefault(uuid, 0);
        boolean hasCloud = CurioManager.hasCloudJar.getOrDefault(uuid, false);
        boolean cloudUsed = CurioManager.usedCloud.getOrDefault(uuid, false);

        if (justPressed &&
                airTicks > 1 &&
                (!hasCloud || CurioManager.usedCloud.getOrDefault(uuid, false)) &&
                !CurioManager.usedRocket.getOrDefault(uuid, false) &&
                !CurioManager.jumpHandledByCloudJar.getOrDefault(uuid, false)) {
            CurioManager.usedRocket.put(uuid, true);
            boostTicks.put(uuid, 1);
            player.level().playSound(player, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        if (jumpNow && CurioManager.usedRocket.getOrDefault(uuid, false) && currentBoost > 0 && currentBoost <= MAX_BOOST_TICKS) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(new Vec3(motion.x, 0.3, motion.z));
            player.hasImpulse = true;
            boostTicks.put(uuid, currentBoost + 1);
        }

        if (!jumpNow || currentBoost > MAX_BOOST_TICKS) {
            boostTicks.put(uuid, 0);
        }

        jumpKeyWasDown.put(uuid, jumpNow);
    }
}
