package com.antihero.enhancedmite.mixin.item;

import com.antihero.enhancedmite.util.SharedUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class)
public abstract class ItemMixin implements ItemConvertible {
    @Shadow public abstract ItemStack getDefaultStack();

    @Shadow @Final private int maxDamage;

    @Shadow public abstract int getMaxDamage();

    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    private void onFinishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        SharedUtils.eatingCooldown = 20;
    }

    @Inject(method = "getMaxDamage", at = @At(value = "HEAD"), cancellable = true)
    private void getMaxDamage(CallbackInfoReturnable<Integer> cir) {
        int maxToolDamage;

        if (getDefaultStack().isOf(Items.NETHERITE_SWORD)) maxToolDamage = 51200;
        else if (getDefaultStack().isOf(Items.NETHERITE_PICKAXE)) maxToolDamage = 153600;
        else if (getDefaultStack().isOf(Items.NETHERITE_AXE)) maxToolDamage = 153600;
        else if (getDefaultStack().isOf(Items.NETHERITE_SHOVEL)) maxToolDamage = 51200;
        else if (getDefaultStack().isOf(Items.NETHERITE_HOE)) maxToolDamage = 102400;
        else maxToolDamage = maxDamage;

        cir.setReturnValue(maxToolDamage);
    }

    @Inject(method = "getItemBarColor", at = @At(value = "HEAD"), cancellable = true)
    private void getItemBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        float f = Math.max(0.0f, ((float)this.getMaxDamage() - (float)stack.getDamage()) / (float)this.getMaxDamage());
        cir.setReturnValue(MathHelper.hsvToRgb(f / 3.0f, 1.0f, 1.0f));
    }
}
