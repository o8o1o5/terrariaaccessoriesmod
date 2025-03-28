package dev.o8o1o5.terrariaaccessories.items.custom;

import dev.o8o1o5.terrariaaccessories.TerrariaAccessories;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class AnkletOfTheWindItem extends Item implements ICurioItem {
    private static final ResourceLocation SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(TerrariaAccessories.MODID, "terrariaaccessories_ankletofthewind_speed_boost");
    private static final double SPEED_BOOST_AMOUNT = 0.1;

    public AnkletOfTheWindItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            AttributeInstance speedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null && !speedAttribute.hasModifier(SPEED_MODIFIER_ID)) {
                AttributeModifier speedModifier = new AttributeModifier(
                        SPEED_MODIFIER_ID,
                        SPEED_BOOST_AMOUNT,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
                speedAttribute.addTransientModifier(speedModifier);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) {
            AttributeInstance speedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null && speedAttribute.hasModifier(SPEED_MODIFIER_ID)) {
                speedAttribute.removeModifier(SPEED_MODIFIER_ID);
            }
        }
    }
}
