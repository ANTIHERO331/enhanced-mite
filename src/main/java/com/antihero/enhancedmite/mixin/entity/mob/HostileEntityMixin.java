package com.antihero.enhancedmite.mixin.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public abstract class HostileEntityMixin extends PathAwareEntity implements Monster {
    @Shadow private int cannotGoToPlayerCounter;

    protected HostileEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canDespawnRightNow", at = @At(value = "HEAD"), cancellable = true)
    private void canDespawnRightNow(CallbackInfoReturnable<Boolean> cir) {
        if (!hasAnyEquipment()) cir.setReturnValue(false);
        else cir.setReturnValue(cannotGoToPlayerCounter > 5);
    }

    /**
     * @author a
     * @reason r
     */
    @Overwrite
    protected boolean isValidLightLevelToSpawn() {
        BlockPos blockPos = new BlockPos(this.getX(), this.getBoundingBox().minY, this.getZ());
        if (this.world.getMoonType().getSurfaceMobCountModifier() > 1.0f &&  this.world.isSkyVisible(this.getBlockPos()))
            return true;

        boolean thundering = this.world.isThundering();
        if (this.getWorld().getDimension() == DimensionType.OVERWORLD && !thundering && this.world.getLightLevel(blockPos) > 0 && blockPos.getY() > world.getTopY())
            return false;

        if (this.world.getBaseLightLevel(blockPos, 0) > this.random.nextInt(32)) return false;

        int i = thundering && (this.getType().equals(EntityType.SPIDER) || this.getType().equals(EntityType.CREEPER)) ?
                (int)((float)this.world.getBaseLightLevel(blockPos, 10) / this.world.getThunderGradient(1.0f))
                :
                this.world.getLightLevel(blockPos);

        return i <= this.random.nextInt(8);
    }

    public boolean hasAnyEquipment() {
        return hasStackEquipped(EquipmentSlot.MAINHAND)
                || hasStackEquipped(EquipmentSlot.OFFHAND)
                || hasStackEquipped(EquipmentSlot.HEAD)
                || hasStackEquipped(EquipmentSlot.CHEST)
                || hasStackEquipped(EquipmentSlot.LEGS)
                || hasStackEquipped(EquipmentSlot.FEET);
    }
}
