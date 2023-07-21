package com.antihero.enhancedmite.mixin.entity.mob;

import net.minecraft.cls_211;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(cls_211.class)
public abstract class RevenantEntityMixin extends ZombieEntity {
    public RevenantEntityMixin(World world) {
        super(world);
    }

    @Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/cls_211;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", ordinal = 0))
    private void equipStack(cls_211 instance, EquipmentSlot slot, ItemStack stack) {
        Random random1 = this.getRandom();
        if (random1.nextInt(3) <= 1) instance.equipStack(slot, Items.RUSTED_IRON_BATTLE_AXE.getDefaultStack());
        else if (random1.nextInt(3) <= 1) instance.equipStack(slot, Items.RUSTED_IRON_WAR_HAMMER.getDefaultStack());
        else instance.equipStack(slot, stack);
    }
}
