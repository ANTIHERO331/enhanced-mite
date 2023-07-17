package com.antihero.enhancedmite.entity.goal;

import com.antihero.enhancedmite.EnhancedMITE;
import com.antihero.enhancedmite.util.EntityInteractionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.NavigationConditions;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldEvents;

public class BreakBlockGoal extends Goal {
    protected HostileEntity mob;
    protected BlockPos targetBlockPos = BlockPos.ORIGIN;
    protected BlockState targetBlock;
    protected boolean blockValid;
    private boolean shouldStop;
    private float offsetX;
    private float offsetZ;
    protected int breakProgress;
    protected int tickToNextBreak;
    private int updateCooldown;

    public BreakBlockGoal(HostileEntity mob) {
        this.mob = mob;
        if (!NavigationConditions.hasMobNavigation(mob)) {
            EnhancedMITE.LOGGER.info("Unsupported mob type for BreakBlockGoal");
        }
    }

    protected boolean isBlockValid() {
        if (mob.world.getBlockState(targetBlockPos).getCollisionShape(mob.world, targetBlockPos) == VoxelShapes.empty()) return false;
        return !mob.world.isAir(this.targetBlockPos) && canZombieHarvest(mob.world.getBlockState(this.targetBlockPos), mob.getMainHandStack());
    }

    private boolean canZombieHarvest(BlockState state, ItemStack stack) {
        return !state.isToolRequired() || stack.isSuitableFor(state);
    }

    public boolean canFindTargetBlock() {
        if (!NavigationConditions.hasMobNavigation(this.mob)) {
            return false;
        }
        MobNavigation mobnavigation = (MobNavigation)this.mob.getNavigation();
        Path path = mobnavigation.getCurrentPath();
        if (path != null && !path.isFinished()) {
            for (int i = 0; i < path.getLength(); ++i) {
                PathNode pathnode = path.getNode(i);
                this.targetBlockPos = new BlockPos(pathnode.x, pathnode.y + 1, pathnode.z);
                if (this.mob.squaredDistanceTo(this.targetBlockPos.getX(), this.mob.getY(), this.targetBlockPos.getZ()) > 2.25) continue;
                this.blockValid = this.isBlockValid();
                if (this.blockValid) {
                    return true;
                }
                this.targetBlockPos = new BlockPos(pathnode.x, pathnode.y, pathnode.z);
                this.blockValid = this.isBlockValid();
                if (!this.blockValid) continue;
                return true;
            }
            this.targetBlockPos = this.mob.getBlockPos().up();
            this.blockValid = this.isBlockValid();
            return this.blockValid;
        }
        return false;
    }

    @Override
    public boolean canStart() {
        if (!canFindTargetBlock() || mob.getTarget() == null) return false;

        if (!mob.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) return false;

        if (mob.distanceTo(mob.getTarget()) >= 5) return false;

        return isBlockValid();
    }

    @Override
    public boolean shouldContinue() {
        return !shouldStop && isBlockValid() && targetBlockPos.isWithinDistance(this.mob.getPos(), EntityInteractionHelper.getReachDistance(this.mob));
    }

    @Override
    public void start() {
        shouldStop = false;
        offsetX = (float)((double)this.targetBlockPos.getX() + 0.5 - this.mob.getX());
        offsetZ = (float)((double)this.targetBlockPos.getZ() + 0.5 - this.mob.getZ());
        mob.getLookControl().lookAt(this.targetBlockPos.getX() + 0.5, this.targetBlockPos.getY() + 0.5, this.targetBlockPos.getZ() + 0.5);
        tickToNextBreak = (int) (this.mob.world.getBlockState(this.targetBlockPos).getHardness(this.mob.world, this.targetBlockPos) * 320.0f);
        targetBlock = this.mob.world.getBlockState(this.targetBlockPos);
        breakProgress = -1;
    }

    @Override
    public void stop() {
        mob.world.setBlockBreakingInfo(this.mob.getId(), this.targetBlockPos, -1);
        breakProgress = -1;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mob.isDead() || this.mob.isRemoved()) {
            shouldStop = true;
            stop();
            return;
        }
        float f = (float)((double)this.targetBlockPos.getX() + 0.5 - this.mob.getX());
        float f2 = this.offsetX * f + this.offsetZ * (float)((double)this.targetBlockPos.getZ() + 0.5 - this.mob.getZ());

        if (f2 < 0.0f) this.shouldStop = true;

        this.mob.getLookControl().lookAt(this.targetBlockPos.getX() + 0.5, this.targetBlockPos.getY() + 0.5, this.targetBlockPos.getZ() + 0.5);

        if (this.tickToNextBreak == 0) {
            ++this.breakProgress;

            this.mob.swingHand(this.mob.getActiveHand());
            this.mob.world.setBlockBreakingInfo(this.mob.getId(), this.targetBlockPos, this.breakProgress);
            this.mob.world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, targetBlockPos, Block.getRawIdFromState(this.mob.world.getBlockState(this.targetBlockPos)));
            if (this.breakProgress >= 10) {
                this.mob.world.breakBlock(this.targetBlockPos, true, this.mob);
                this.breakProgress = -1;
            }

            this.tickToNextBreak = (int) (targetBlock.getHardness(this.mob.world, this.targetBlockPos) * 320.0f);

            if (this.mob.getMainHandStack().isSuitableFor(targetBlock)) {
                this.tickToNextBreak = (int) ((float) this.tickToNextBreak / this.mob.getMainHandStack().getMiningSpeedMultiplier(targetBlock));
            }
        } else {
            --this.tickToNextBreak;
        }
    }
}
