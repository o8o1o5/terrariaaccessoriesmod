package dev.o8o1o5.terrariaaccessories.items.custom;

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

public class HermesBootsItem extends Item implements ICurioItem {
    private static final Map<UUID, Integer> sprintTimer = new HashMap<>();
    private static final double MAX_BONUS = 0.325;
    private static final double BONUS_PER_TICK = 0.00125;

    public HermesBootsItem(Properties properties) {
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

        }
    }
}
