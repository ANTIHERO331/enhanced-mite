package com.antihero.enhancedmite.mixin.entity.mob;

import com.antihero.enhancedmite.entity.goal.BreakBlockGoal;
import net.minecraft.cls_187;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {cls_187.class})
public abstract class GhoulEntityMixin extends ZombieEntity {
    public GhoulEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "applyEntityAI", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 0))
    private void add(GoalSelector instance, int priority, Goal goal) {
        instance.add(priority, new BreakBlockGoal(this));
    }
}
