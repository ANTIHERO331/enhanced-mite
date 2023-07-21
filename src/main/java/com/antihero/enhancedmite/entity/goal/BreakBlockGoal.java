package com.antihero.enhancedmite.entity.goal;

import com.antihero.enhancedmite.util.EntityInteractionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.cls_187;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.WorldEvents;

import java.util.*;

/**
 * Notice: the codes below is from mod EnhancedAI on the GitHub website:
 * <p>
 *
 * <a href="https://github.com/Insane96/EnhancedAI">EnhancedAI GitHub</a>
 */
public class BreakBlockGoal extends Goal {
    private final MobEntity mob;
    private LivingEntity target;
    private final double reachDistance;
    private List<BlockPos> targetBlockPositions = new ArrayList<>();
    private int breakProgress = -1;
    private BlockState blockState = null;
    private int prevBreakProgress = -1;

    private Vec3d lastPosition = null;
    private int lastPositionTicks = 0;

    private Path path = null;

    public BreakBlockGoal(MobEntity mob){
        this.mob = mob;
        this.reachDistance = EntityInteractionHelper.getReachDistance(mob);
        this.setControls(EnumSet.of(Control.LOOK, Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.mob.world.getDifficulty() != Difficulty.HARD) return false;

        if (this.mob.getTarget() == null) return false;

        return this.isStuck() && this.mob.squaredDistanceTo(mob.getTarget()) > EntityInteractionHelper.getAttackDistance(this.mob) * EntityInteractionHelper.getAttackDistance(this.mob)
                && this.mob.squaredDistanceTo(mob.getTarget()) < 25;
    }

    @Override
    public boolean shouldContinue() {
        if (this.blockState != null && !this.canHarvest(this.blockState, this.mob.getMainHandStack())) {
            this.mob.world.setBlockBreakingInfo(this.mob.getId(), targetBlockPositions.get(0), -1);
            return false;
        }

        if (this.target == null || !this.target.isAlive()) return false;

        if (this.mob.isDead()) return false;

        return !this.targetBlockPositions.isEmpty()
                && getMinSquaredDistance(mob.getEyePos(), target.getPos(), target) > EntityInteractionHelper.getAttackDistance(this.mob)
                && this.targetBlockPositions.get(0).getSquaredDistance(this.mob.getBlockPos()) < this.reachDistance * this.reachDistance
                && this.mob.getNavigation().isIdle()
                && !this.mob.world.getBlockState(this.targetBlockPositions.get(0)).isAir()
                && this.path != null && this.path.getManhattanDistanceFromTarget() > 1.5d;
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

    @Override
    public void start() {
        this.target = this.mob.getTarget();
        if (this.target == null) return;
        findTargetBlockPositions$1();
        findTargetBlockPositions$2();
        if (!this.targetBlockPositions.isEmpty()) {
            targetBlockPositions = removeDuplication(targetBlockPositions);
            initBlockBreak();
        }
    }

    @Override
    public void stop() {
        this.target = null;
        if (!this.targetBlockPositions.isEmpty()) {
            this.mob.world.setBlockBreakingInfo(this.mob.getId(), targetBlockPositions.get(0), -1);
            this.targetBlockPositions.clear();
        }
        this.breakProgress = -1;
        this.prevBreakProgress = -1;
        this.blockState = null;
        this.lastPosition = null;
        this.path = null;
    }

    @Override
    public void tick() {
        if (this.targetBlockPositions.isEmpty()) return;
        if (this.blockState != null && !this.canHarvest(this.blockState, this.mob.getMainHandStack())) return;
        ++this.breakProgress;
        this.mob.getLookControl().lookAt(this.targetBlockPositions.get(0).getX() + 0.5, this.targetBlockPositions.get(0).getY() + 0.5, this.targetBlockPositions.get(0).getZ() + 0.5);
        int adjustedStage;
        int stage = MathHelper.floor(this.breakProgress / (float)this.getMaxBreakProgress() * 10.0f);
        if ((adjustedStage = stage - 1) != this.prevBreakProgress) {
            this.mob.getLookControl().lookAt(this.targetBlockPositions.get(0).getX() + 0.5, this.targetBlockPositions.get(0).getY() + 0.5, this.targetBlockPositions.get(0).getZ() + 0.5);
            this.mob.getNavigation().stop();
            this.mob.swingHand(this.mob.getActiveHand());
            this.mob.world.setBlockBreakingInfo(this.mob.getId(), this.targetBlockPositions.get(0), adjustedStage);
            this.mob.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, this.targetBlockPositions.get(0), Block.getRawIdFromState(this.mob.world.getBlockState(this.targetBlockPositions.get(0))));
            this.prevBreakProgress = adjustedStage;
        }
        if (this.breakProgress >= this.getMaxBreakProgress()) {
            this.mob.world.breakBlock(this.targetBlockPositions.get(0), true, this.mob);
            this.mob.world.setBlockBreakingInfo(this.mob.getId(), targetBlockPositions.get(0), -1);
            if (!this.mob.getMainHandStack().isEmpty() && this.mob.getMainHandStack().getItem() instanceof ToolItem) {
                this.mob.getMainHandStack().damage((int) this.blockState.getHardness(this.mob.world, this.targetBlockPositions.get(0)), this.mob, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            }
            if (this.targetBlockPositions.size() > 0) {
                this.targetBlockPositions.remove(0);
            }
            if (!this.targetBlockPositions.isEmpty()) initBlockBreak();
            else if (this.mob.squaredDistanceTo(this.target) > EntityInteractionHelper.getAttackDistance(this.mob) * EntityInteractionHelper.getAttackDistance(this.mob)
                    && !this.mob.getVisibilityCache().canSee(this.target)) start();
        }
    }

    private void initBlockBreak() {
        this.blockState = this.mob.world.getBlockState(this.targetBlockPositions.get(0));
        this.breakProgress = 0;
        this.path = this.mob.getNavigation().findPathTo(this.target, 1);
    }

    private <E extends BlockPos> List<E> removeDuplication(List<E> list) {
        Set<E> temp = new HashSet<>(list);
        list = new ArrayList<>(temp);
        return list;
    }

    private void findTargetBlockPositions$1() {
        if (!NavigationConditions.hasMobNavigation(this.mob)) return;

        MobNavigation mobnavigation = (MobNavigation)this.mob.getNavigation();
        Path path = mobnavigation.getCurrentPath();

        if (path != null && !path.isFinished()) {
            for (int i = 0; i < path.getLength(); ++i) {
                PathNode pathnode = path.getNode(i);

                this.targetBlockPositions.add(new BlockPos(pathnode.x, pathnode.y + 1, pathnode.z)) ;

                if (this.mob.squaredDistanceTo(this.targetBlockPositions.get(0).getX(), this.mob.getY(), this.targetBlockPositions.get(0).getZ()) > 2.25d) continue;

                this.targetBlockPositions.add(new BlockPos(pathnode.x, pathnode.y, pathnode.z));
            }
            this.targetBlockPositions.add(this.mob.getBlockPos().up());
        }
        Collections.reverse(targetBlockPositions);
    }

    private void findTargetBlockPositions$2() {
        int mobHeight = MathHelper.ceil(this.mob.getHeight());
        for (int i = 0; i < mobHeight; i++) {
            BlockHitResult blockHitResult = this.mob.world.raycast(new RaycastContext(this.mob.getPos().add(0, i + 0.5d, 0), this.target.getEyePos().add(0, i, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.mob));
            if (blockHitResult.getType() == HitResult.Type.MISS) continue;

            if (this.targetBlockPositions.contains(blockHitResult.getBlockPos())) continue;

            double distance = this.mob.squaredDistanceTo(blockHitResult.getPos());
            if (distance > this.reachDistance * this.reachDistance) continue;

            BlockState state = this.mob.world.getBlockState(blockHitResult.getBlockPos());
            if (state.hasBlockEntity() || state.getHardness(this.mob.world, blockHitResult.getBlockPos()) == -1) continue;

            if (state.hasBlockEntity()) continue;

            this.targetBlockPositions.add(blockHitResult.getBlockPos());
        }
        Collections.reverse(this.targetBlockPositions);
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    public boolean isStuck() {
        if (this.mob.getTarget() == null) return false;
        if (this.lastPosition == null || this.mob.squaredDistanceTo(this.lastPosition) > 2.25d) {
            this.lastPosition = this.mob.getPos();
            this.lastPositionTicks = this.mob.age;
        }
        return this.mob.getNavigation().isIdle() || this.mob.age - this.lastPositionTicks >= 60;
    }

    private int getMaxBreakProgress() {
        BlockState state = this.mob.world.getBlockState(this.targetBlockPositions.get(0));
        float miningSpeedMultiplier = this.getBlockBreakingSpeed();
        float returnValue = 3000 * (state.getHardness(this.mob.world, this.targetBlockPositions.get(0)) / miningSpeedMultiplier);
        if (mob instanceof cls_187) returnValue /= 2;
        return (int) returnValue;
    }

    private float getBlockBreakingSpeed() {
        float miningSpeed = this.mob.getMainHandStack().getMiningSpeedMultiplier(this.blockState);
        if (miningSpeed > 1.0F) {
            int efficiencyLevel = EnchantmentHelper.getEfficiency(this.mob);
            ItemStack itemstack = this.mob.getMainHandStack();
            if (efficiencyLevel > 0 && !itemstack.isEmpty()) {
                miningSpeed += (float)(efficiencyLevel * efficiencyLevel + 1);
            }
        }
        if (StatusEffectUtil.hasHaste(this.mob)) {
            miningSpeed *= 1.0F + (float)(StatusEffectUtil.getHasteAmplifier(this.mob) + 1) * 0.2F;
        }
        if (this.mob.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float miningFatigueAmplifier = switch (Objects.requireNonNull(this.mob.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            miningSpeed *= miningFatigueAmplifier;
        }
        if (this.mob.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this.mob)) {
            miningSpeed /= 5.0F;
        }
        return miningSpeed;
    }

    private boolean canHarvest(BlockState state, ItemStack stack) {
        return !state.isToolRequired() || stack.isSuitableFor(this.blockState);
    }
}