package com.antihero.enhancedmite.mixin.entity.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.cls_125;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.antihero.enhancedmite.util.SharedUtils.eatingCooldown;

@Mixin(value = {PlayerEntity.class})
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow @Final private PlayerAbilities abilities;

    @Shadow protected HungerManager hungerManager;

    @Shadow public abstract boolean hasCurse(cls_125 curse);

    @Shadow public abstract void addExhaustionWithEndurance(float exhaustion);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tickEatingCooldown(CallbackInfo ci) {
        if (eatingCooldown > 0) --eatingCooldown;

        if (MinecraftClient.getInstance().interactionManager != null && MinecraftClient.getInstance().interactionManager.isBreakingBlock()) this.addExhaustionWithEndurance(0.008f);
    }

    /**
     * @author a
     * @reason r
     */
    @Overwrite
    public boolean canConsume(boolean ignoreHunger) {
        if ((this.hasStatusEffect(StatusEffects.INSULIN_RESISTANCE) && this.getStatusEffect(StatusEffects.INSULIN_RESISTANCE).getAmplifier() >= 2) || hasStatusEffect(StatusEffects.NAUSEA))
            return false;
        return this.abilities.invulnerable || ignoreHunger || this.hungerManager.isNotFull();
    }

    /**
     * @author a
     * @reason r
     */
    @Overwrite
    public boolean canConsume(FoodComponent foodComponent) {
        if (eatingCooldown > 0) return false;

        if (hasStatusEffect(StatusEffects.NAUSEA)) return false;

        if (this.hasStatusEffect(StatusEffects.INSULIN_RESISTANCE) && this.getStatusEffect(StatusEffects.INSULIN_RESISTANCE).getAmplifier() >= 2 && foodComponent.getInsulinResponse() != 0)
            return false;

        if (foodComponent.isMeat() ? this.hasCurse(cls_125.CANNOT_EAT_MEAT) : this.hasCurse(cls_125.CANNOT_EAT_PLANT))
            return false;

        if (foodComponent.isSoup() && this.hasCurse(cls_125.CANNOT_DRINK)) return false;
        return this.abilities.invulnerable || foodComponent.isAlwaysEdible() || this.hungerManager.isNotFull();
    }
}
