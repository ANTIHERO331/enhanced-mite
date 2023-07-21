package com.antihero.enhancedmite.mixin.entity.ai.goal;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T extends LivingEntity> extends TrackTargetGoal {
    @Shadow @Nullable protected LivingEntity targetEntity;

    @Shadow @Final protected Class<T> targetClass;

    @Shadow protected abstract Box getSearchBox(double distance);

    @Shadow protected TargetPredicate targetPredicate;

    @Shadow private int retargetTimer;

    public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility, boolean checkNavigable) {
        super(mob, checkVisibility, checkNavigable);
    }

    @Inject(method = "getSearchBox", at = @At(value = "HEAD"), cancellable = true)
    private void setSearchBox(double distance, CallbackInfoReturnable<Box> cir) {
        cir.setReturnValue(this.mob.getBoundingBox().expand(distance, distance, distance));
    }

    /**
     * @author a
     * @reason r
     */
    @Overwrite
    protected void findClosestTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        this.targetEntity =
                this.targetClass == PlayerEntity.class
                        || this.targetClass == ServerPlayerEntity.class
                        || client.interactionManager == null
                        || client.interactionManager.isBreakingBlock()
                        || client.player == null
                        || client.player.isUsingItem() ?
                        this.mob.world.getClosestPlayer(
                                this.targetPredicate,
                                this.mob,
                                this.mob.getX(),
                                this.mob.getEyeY(),
                                this.mob.getZ()
                        )
                        :
                        this.mob.world.getClosestEntity(
                                this.mob.world.getEntitiesByClass(this.targetClass, this.getSearchBox(this.getFollowRange()), livingEntity -> true),
                                this.targetPredicate,
                                this.mob,
                                this.mob.getX(),
                                this.mob.getEyeY(),
                                this.mob.getZ()
                        );
    }
}
