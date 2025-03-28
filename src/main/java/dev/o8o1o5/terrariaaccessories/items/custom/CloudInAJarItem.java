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
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudInAJarItem extends Item implements ICurioItem {
    private static final Map<UUID, Integer> ticksInAir = new HashMap<>();
    private static final Map<UUID, Boolean> jumpKeyWasDown = new HashMap<>();

    public CloudInAJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) return;
        UUID uuid = player.getUUID();

        // Cloud 착용 여부 저장
        CurioManager.hasCloudJar.put(uuid, true);

        if (!player.level().isClientSide) {
            if (player.onGround()) {
                CurioManager.usedCloud.put(uuid, false);
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

        // ✅ 먼저 Cloud 사용 처리
        if (justPressed && airTicks > 1 && !CurioManager.usedCloud.getOrDefault(uuid, false)) {
            CurioManager.usedCloud.put(uuid, true); // 먼저 표시!
            CurioManager.jumpHandledByCloudJar.put(uuid, true);

            player.setDeltaMovement(player.getDeltaMovement().x, 0.5, player.getDeltaMovement().z);
            player.hasImpulse = true;


            for (int i = 0; i < 25; i++) { // 성글하게 하려면 개수는 줄이고
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 2; // -1.25 ~ +1.25
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 2;
                double velY = 0.02 + player.getRandom().nextDouble() * 0.03; // 살짝만 뜨게

                player.level().addParticle(ParticleTypes.CLOUD,
                        player.getX() + offsetX,
                        player.getY() + 0.1,
                        player.getZ() + offsetZ,
                        0.0, velY, 0.0);
            }
            player.level().playSound(player, player.blockPosition(), SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        CurioManager.jumpHandledByCloudJar.put(uuid, false);
        jumpKeyWasDown.put(uuid, jumpNow);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        CurioManager.hasCloudJar.put(slotContext.entity().getUUID(), true);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        CurioManager.hasCloudJar.put(slotContext.entity().getUUID(), false);
    }
}
