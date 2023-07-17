package com.antihero.enhancedmite.mixin.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Block.class})
public abstract class BlockMixin extends AbstractBlock {
    @Shadow protected abstract Block asBlock();

    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onSteppedOn", at = @At(value = "HEAD"))
    private void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (this.asBlock() instanceof FallingBlock block) {
            world.createAndScheduleBlockTick(pos, block, 2);
        }
    }

    @Inject(method = "onBreak", at = @At(value = "HEAD"))
    private void onGrassBlockBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        BlockPos upperPos;
        BlockState upperState;
        if (state.isOf(Blocks.GRASS_BLOCK) && (upperState = world.getBlockState(upperPos = pos.up())).isIn(BlockTags.REPLACEABLE_PLANTS)) {
            world.setBlockState(upperPos, Blocks.AIR.getDefaultState());
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, upperPos, Block.getRawIdFromState(upperState));
            world.spawnEntity(new ItemEntity(world, upperPos.getX() + 0.5, upperPos.getY() + 0.5, upperPos.getZ() + 0.5, new ItemStack(Items.GRASS, 1)));
        }
    }
}
