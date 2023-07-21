package com.antihero.enhancedmite.mixin.item;

import com.antihero.enhancedmite.item.NetheriteArmorMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorItem.class)
public abstract class ArmorItemMixin extends Item {
    private static final NetheriteArmorMaterial MATERIAL = new NetheriteArmorMaterial();

    @Shadow public abstract ArmorMaterial getMaterial();

    public ArmorItemMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial;getDurability(Lnet/minecraft/entity/EquipmentSlot;)I"))
    private static int getDurability(ArmorMaterial instance, EquipmentSlot slot) {
        if (instance == ArmorMaterials.NETHERITE) {
            return MATERIAL.getDurability(slot);
        }
        return instance.getDurability(slot);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial;getProtectionAmount(Lnet/minecraft/entity/EquipmentSlot;)F"))
    private float getProtectionAmount(ArmorMaterial instance, EquipmentSlot slot) {
        if (instance == ArmorMaterials.NETHERITE) {
            return MATERIAL.getProtectionAmount(slot);
        }
        return instance.getProtectionAmount(slot);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial;getToughness()F"))
    private float getToughness(ArmorMaterial instance) {
        if (instance == ArmorMaterials.NETHERITE) {
            return MATERIAL.getToughness();
        }
        return instance.getToughness();
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial;getKnockbackResistance()F"))
    private float getKnockbackResistance(ArmorMaterial instance) {
        if (instance == ArmorMaterials.NETHERITE) {
            return MATERIAL.getKnockbackResistance();
        }
        return instance.getKnockbackResistance();
    }
}
