package com.antihero.enhancedmite.item;

import com.antihero.enhancedmite.EnhancedMITE;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;

public class NetheriteArmorMaterial implements ArmorMaterial {
    private final int[] maxDamageArray = new int[]{1024, 1792, 2048, 1280};

    @Override
    public int getDurability(EquipmentSlot slot) {
        return maxDamageArray[slot.getEntitySlotId()];
    }

    @Override
    public float getProtectionAmount(EquipmentSlot slot) {
        float[] protectionArray = new float[]{1.7f, 2.9f, 3.3f, 2.1f};
        return protectionArray[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return 60;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return new Lazy<>(() -> Ingredient.ofItems(EnhancedMITE.NETHERITE_NUGGET)).get();
    }

    @Override
    public int getRepairDurability() {
        return this.maxDamageArray[1] / 16;
    }

    @Override
    public int getRepairLevel() {
        return ToolMaterials.NETHERITE.getRepairLevel();
    }

    @Override
    public String getName() {
        return "netherite";
    }

    @Override
    public float getToughness() {
        return 3.0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.2f;
    }

    @Override
    public int getAcidResistance() {
        return 10000;
    }
}
