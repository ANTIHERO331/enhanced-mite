package com.antihero.enhancedmite.mixin.entity.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TrackTargetGoal.class)
public abstract class TrackTargetGoalMixin extends Goal {
    @Shadow @Nullable protected LivingEntity target;

    @Shadow @Final protected MobEntity mob;

    @Redirect(method = "shouldContinue", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D"))
    private double squaredDistanceTo(MobEntity instance, Entity entity) {
        return -1;
    }

    /**
     * @author a
     * @reason r
     */
    @Overwrite
    public void stop() {
        if (this.target != null && (this.target.isDead() || this.target.isRemoved())) {
            this.mob.setTarget(null);
            this.target = null;
        }
    }
}
