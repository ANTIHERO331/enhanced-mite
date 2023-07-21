package com.antihero.enhancedmite.mixin.entity.ai.goal;

import com.antihero.enhancedmite.util.EntityInteractionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin extends Goal {
    @Shadow @Final protected PathAwareEntity mob;

    @Shadow private Path path;

    @Shadow @Final protected double speed;

    @Shadow protected int cooldown;

    @Shadow protected double targetX;

    @Shadow protected double targetY;

    @Shadow protected double targetZ;

    @Shadow protected int updateCountdownTicks;

    @Shadow @Final protected boolean pauseWhenMobIdle;

    @Shadow protected abstract void resetCooldown();

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    public void tick() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null) {
            this.mob.getLookControl().lookAt(livingentity, 30.0f, 30.0f);
            Vec3d mobEyePos = mob.getEyePos();
            double d0 = getMinSquaredDistance(mobEyePos, livingentity.getPos(), livingentity);
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingentity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingentity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.targetX = livingentity.getX();
                this.targetY = livingentity.getY();
                this.targetZ = livingentity.getZ();
                this.updateCountdownTicks = 10 + this.mob.getRandom().nextInt(7);
                if (d0 > 4096.0) {
                    this.updateCountdownTicks += 10;
                } else if (d0 > 1024.0) {
                    this.updateCountdownTicks += 5;
                }
                EntityNavigation navigation = this.mob.getNavigation();
                Path path = navigation.findPathTo(livingentity, 0);
                if (path != null && navigation.startMovingAlong(path, this.speed)) {
                    this.updateCountdownTicks += 15;
                }
                this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
            }
            this.attack(livingentity, d0);
            this.cooldown = Math.max(this.cooldown - 1, 0);
        }
    }

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    protected void attack(LivingEntity target, double squaredDistance) {
        if (squaredDistance <= this.getSquaredMaxAttackDistance(this.mob) && this.cooldown <= 0 && canMobReachToTarget(this.mob, target)) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(target);
        }
    }

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    protected double getSquaredMaxAttackDistance(Entity entity) {
        return getDistanceMultiplier(entity) * getDistanceMultiplier(entity);
    }

    private double getDistanceMultiplier(Entity entity) {
        return EntityInteractionHelper.getAttackDistance((LivingEntity) entity) + entity.getWidth() / 2;
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean canMobReachToTarget(PathAwareEntity mob, LivingEntity target) {
        if (target.world != mob.world) return false;

        Vec3d mobEyePos = new Vec3d(mob.getX(), mob.getEyeY(), mob.getZ());
        Vec3d mobPos = new Vec3d(mob.getX(), mob.getY(), mob.getZ());

        Vec3d targetEyePos = new Vec3d(target.getX(), target.getEyeY(), target.getZ());
        Vec3d targetPos = new Vec3d(target.getX(), target.getY(), target.getZ());

        if (mobPos.distanceTo(targetPos) > 128.0) return false;

        boolean bl1 = mob.world.raycast(new RaycastContext(mobEyePos, targetEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mob)).getType() == HitResult.Type.MISS;
        boolean bl2 = mob.world.raycast(new RaycastContext(mobEyePos, targetPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mob)).getType() == HitResult.Type.MISS;
        boolean bl3 = mob.world.raycast(new RaycastContext(mobPos, targetEyePos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mob)).getType() == HitResult.Type.MISS;
        boolean bl4 = mob.world.raycast(new RaycastContext(mobPos, targetPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mob)).getType() == HitResult.Type.MISS;

        return bl1 || bl2 || bl3 || bl4;
    }

    private double getMinSquaredDistance(Vec3d attackerPos, Vec3d targetPos, Entity target) {
        List<Vec3d> list = new ArrayList<>();
        List<Double> squaredDistances = new ArrayList<>();
        for (double d = 0.000; d <= target.getHeight(); d += 0.020d) {
            list.add(new Vec3d(targetPos.x, targetPos.y + d, targetPos.z));
        }
        for (Vec3d temp : list) {
            double squaredDistance = attackerPos.squaredDistanceTo(temp);
            squaredDistances.add(squaredDistance);
        }
        return Collections.min(squaredDistances);
    }
}
