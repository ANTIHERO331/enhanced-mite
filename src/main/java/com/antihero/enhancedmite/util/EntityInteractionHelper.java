package com.antihero.enhancedmite.util;

import net.minecraft.cls_105;
import net.minecraft.cls_32;
import net.minecraft.cls_45;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import org.jetbrains.annotations.NotNull;

public final class EntityInteractionHelper {
    public static double getAttackDistance(@NotNull LivingEntity entity, double baseValue) {
        ItemStack mainHandStack = entity.getMainHandStack();
        Item mainHandItem = mainHandStack.getItem();

        return getExtraReach(mainHandStack, mainHandItem, baseValue);
    }

    public static double getAttackDistance(@NotNull LivingEntity entity) {
        ItemStack mainHandStack = entity.getMainHandStack();
        Item mainHandItem = mainHandStack.getItem();

        return getExtraReach(mainHandStack, mainHandItem, 1.5);
    }

    public static double getReachDistance(@NotNull LivingEntity entity) {
        ItemStack mainHandStack = entity.getMainHandStack();
        Item mainHandItem = mainHandStack.getItem();

        return getExtraReach(mainHandStack, mainHandItem, 2.75);
    }

    public static double getExtraReachValue(LivingEntity entity, ItemStack stack) {
        if (stack.isOf(Items.STICK) || stack.isOf(Items.BONE) || stack.getItem() instanceof cls_105 || stack.getItem() instanceof cls_32)
            return 0.5;

        if (stack.getItem() instanceof ToolItem) {

            if (stack.getItem() instanceof cls_45)
                return 1.0;

            return 0.75;
        }

        if (stack.getItem() instanceof ShearsItem)
            return 1.0;

        return 0;
    }

    private static double getExtraReach(@NotNull ItemStack mainHandStack, Item mainHandItem, double base) {
        if (mainHandStack.isOf(Items.STICK) || mainHandStack.isOf(Items.BONE) || mainHandItem instanceof cls_105 || mainHandItem instanceof cls_32)
            return base + 0.5;

        if (mainHandItem instanceof ToolItem) {

            if (mainHandItem instanceof cls_45)
                return base + 1.0;

            return base + 0.75;
        }

        if (mainHandItem instanceof ShearsItem)
            return base + 1.0;

        return base;
    }

    private EntityInteractionHelper() {}
}
