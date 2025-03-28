package dev.o8o1o5.terrariaaccessories.items.custom;

import dev.o8o1o5.terrariaaccessories.core.CurioManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
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

public class SpectreBootsItem extends Item implements ICurioItem {
    private static final Map<UUID, Integer> sprintTimer = new HashMap<>();
    private static final double MAX_BONUS = 0.325;
    private static final double BONUS_PER_TICK = 0.00125;

    private static final Map<UUID, Integer> boostTicks = new HashMap<>();
    private static final Map<UUID, Integer> ticksInAir = new HashMap<>();
    private static final Map<UUID, Boolean> jumpKeyWasDown = new HashMap<>();
    private static final int MAX_BOOST_TICKS = 16; // 1.2초

    public SpectreBootsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Player player = (Player) entity;
        UUID uuid = player.getUUID();
        int ticks = sprintTimer.getOrDefault(uuid, 0);

        if (player.level().isClientSide && player.onGround()) {
            if (player.isSprinting()) {
                ticks++;

                double bonus = Math.min(ticks * BONUS_PER_TICK, MAX_BONUS);
                Vec3 look = player.getLookAngle().normalize();
                Vec3 current = player.getDeltaMovement();
                Vec3 bonusVec = new Vec3(look.x * bonus, 0, look.z * bonus);
                Vec3 result = current.add(bonusVec);

                double horizontalSpeed = Math.sqrt(result.x * result.x + result.z * result.z);
                if (horizontalSpeed > MAX_BONUS ) {
                    double scale = MAX_BONUS / horizontalSpeed;
                    result = new Vec3(result.x * scale, result.y, result.z * scale);
                }

                player.setDeltaMovement(result);
                sprintTimer.put(uuid, ticks);
            } else {
                sprintTimer.put(uuid, 0);
            }
        }

        if (!player.level().isClientSide) {
            if (player.onGround()) {
                CurioManager.usedSpectreRocket.put(uuid, false);
                boostTicks.put(uuid, 0);
                ticksInAir.put(uuid, 0);
            }
            return;
        }

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

        if (justPressed &&
                airTicks > 1 &&
                (!hasCloud || CurioManager.usedCloud.getOrDefault(uuid, false)) &&
                !CurioManager.usedSpectreRocket.getOrDefault(uuid, false) &&
                !CurioManager.jumpHandledByCloudJar.getOrDefault(uuid, false)) {
            CurioManager.usedSpectreRocket.put(uuid, true);
            boostTicks.put(uuid, 1);
            player.level().playSound(player, player.blockPosition(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        if (jumpNow && CurioManager.usedSpectreRocket.getOrDefault(uuid, false) && currentBoost > 0 && currentBoost <= MAX_BOOST_TICKS) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(new Vec3(motion.x, 0.3, motion.z));
            player.hasImpulse = true;
            boostTicks.put(uuid, currentBoost + 1);

            player.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0, -0.1, 0);
        }


        if (!jumpNow || currentBoost > MAX_BOOST_TICKS) {
            boostTicks.put(uuid, 0);
        }

        jumpKeyWasDown.put(uuid, jumpNow);
    }
}
