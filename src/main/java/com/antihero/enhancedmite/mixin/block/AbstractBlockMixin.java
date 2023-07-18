package com.antihero.enhancedmite.mixin.block;

import com.antihero.enhancedmite.EnhancedMITE;
import net.minecraft.block.*;
import net.minecraft.cls_111;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.block.Blocks.*;

@Mixin(value = {AbstractBlock.class})
public abstract class AbstractBlockMixin {

    @Inject(method = "calcBlockBreakingDelta", at = @At(value = "HEAD"), cancellable = true)
    private void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        HungerManager manager = player.getHungerManager();
        float hardness = state.getHardness(world, pos);
        float breakingSpeed = state.isIn(EnhancedMITE.POTABLE) ? 1 : player.getBlockBreakingSpeed(state);
        if (state.isIn(EnhancedMITE.POTABLE)) hardness /= 160.0f;

        if (!player.canHarvest(state))
            cir.setReturnValue(0.0f);
        else if (manager.getFoodLevel() <= 0 && manager.getSaturationLevel() <= 0)
            cir.setReturnValue(breakingSpeed / hardness / 2000.0f);
        else
            cir.setReturnValue(breakingSpeed / hardness / 600.0f);
    }

    @Inject(method = "onBlockAdded", at = @At(value = "HEAD"))
    private void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
        if (state.isIn(BlockTags.REPLACEABLE_PLANTS) && !state.isIn(BlockTags.FLOWERS) && !state.isOf(DEAD_BUSH)) {
            if (world.getBlockState(pos).isOf(state.getBlock())  && world.getBlockState(pos.down()).isOf(DIRT)) {
                world.setBlockState(pos.down(), GRASS_BLOCK.getDefaultState());
            }
        }
        if (state.getOutlineShape(world, pos) == VoxelShapes.fullCube() && world.getBlockState(pos.down()).isOf(GRASS_BLOCK)) {
            world.setBlockState(pos.down(), DIRT.getDefaultState());
        }
    }

    @Mixin(value = {AbstractBlock.AbstractBlockState.class})
    private abstract static class AbstractBlockStateMixin {
        @Mutable
        @Shadow @Final private float hardness;

        @Shadow public abstract boolean isOf(Block block);

        @Shadow public abstract boolean isIn(TagKey<Block> tag);

        @Shadow public abstract Block getBlock();

        @Inject(method = "getHardness", at = @At(value = "HEAD"), cancellable = true)
        private void setHardness(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
            float blockHardness;

            if (isIn(BlockTags.REPLACEABLE_PLANTS) || isIn(BlockTags.SAPLINGS)) blockHardness = 0.02f;
            else if (isOf(SUGAR_CANE)) blockHardness = 0.08f;

            else if (isOf(DIRT)) blockHardness = 0.5f;
            else if (isOf(GRASS_BLOCK)) blockHardness = 0.6f;
            else if (isOf(PODZOL)) blockHardness = 0.6f;
            else if (isOf(COARSE_DIRT)) blockHardness = 0.6f;
            else if (isOf(MYCELIUM)) blockHardness = 0.6f;
            else if (isOf(ROOTED_DIRT)) blockHardness = 0.6f;
            else if (isOf(DIRT_PATH)) blockHardness = 0.7f;
            else if (isOf(GRAVEL)) blockHardness = 0.6f;
            else if (isOf(MOSS_BLOCK)) blockHardness = 0.1f;
            else if (isOf(SPONGE)) blockHardness = 0.6f;
            else if (isOf(WET_SPONGE)) blockHardness = 0.6f;
            else if (isOf(GLASS_PANE)) blockHardness = 0.1f;
            else if (isOf(DISPENSER)) blockHardness = 3.5f;
            else if (isOf(SANDSTONE)) blockHardness = 0.8f;
            else if (isOf(CHISELED_SANDSTONE)) blockHardness = 0.8f;
            else if (isOf(CUT_SANDSTONE)) blockHardness = 0.8f;
            else if (isOf(NOTE_BLOCK)) blockHardness = 0.8f;
            else if (isOf(POWERED_RAIL) || isOf(DETECTOR_RAIL)) blockHardness = 0.7f;
            else if (isOf(STICKY_PISTON) || isOf(PISTON) || isOf(PISTON_HEAD)) blockHardness = 0.5f;
            else if (isOf(COBWEB)) blockHardness = 0.1f;
            else if (isOf(BRICKS)) blockHardness = 2.0f;
            else if (isOf(TNT)) blockHardness = 1.0f;
            else if (isOf(BOOKSHELF)) blockHardness = 1.5f;
            else if (isOf(MOSSY_COBBLESTONE)) blockHardness = 2.0f;
            else if (isOf(SPAWNER)) blockHardness = 3.0f;
            else if (isOf(FARMLAND)) blockHardness = 0.6f;
            else if (isOf(RAIL)) blockHardness = 0.7f;
            else if (isOf(COBBLESTONE_STAIRS)) blockHardness = 2.0f;
            else if (isOf(LEVER)) blockHardness = 0.5f;
            else if (isOf(IRON_DOOR)) blockHardness = 4.0f;
            else if (isOf(SNOW)) blockHardness = 0.05f;
            else if (isOf(ICE)) blockHardness = 1.0f;
            else if (isOf(SNOW_BLOCK)) blockHardness = 0.4f;
            else if (isOf(CACTUS)) blockHardness = 0.4f;
            else if (isOf(CLAY)) blockHardness = 0.8f;
            else if (isOf(JUKEBOX)) blockHardness = 2.0f;
            else if (isOf(PUMPKIN)) blockHardness = 0.6f;
            else if (isOf(NETHERRACK)) blockHardness = 1.6f;
            else if (isOf(SOUL_SAND)) blockHardness = 0.5f;
            else if (isOf(SOUL_SOIL)) blockHardness = 0.5f;
            else if (isOf(BASALT)) blockHardness = 2.4f;
            else if (isOf(POLISHED_BASALT)) blockHardness = 2.4f;
            else if (isOf(GLOWSTONE)) blockHardness = 0.3f;
            else if (isOf(CARVED_PUMPKIN)) blockHardness = 0.6f;
            else if (isOf(JACK_O_LANTERN)) blockHardness = 1.0f;
            else if (isOf(CAKE)) blockHardness = 0.5f;
            else if (isOf(SMITHING_TABLE)) blockHardness = 1.0f;

            else if (isOf(STONE)
                    || isOf(GRANITE)
                    || isOf(POLISHED_GRANITE)
                    || isOf(GRANITE_SLAB)
                    || isOf(POLISHED_GRANITE_SLAB)
                    || isOf(GRANITE_STAIRS)
                    || isOf(POLISHED_GRANITE_STAIRS)
                    || isOf(ANDESITE)
                    || isOf(POLISHED_ANDESITE)
                    || isOf(ANDESITE_SLAB)
                    || isOf(POLISHED_ANDESITE_SLAB)
                    || isOf(ANDESITE_STAIRS)
                    || isOf(POLISHED_ANDESITE_STAIRS)
                    || isOf(DIORITE)
                    || isOf(POLISHED_DIORITE)
                    || isOf(DIORITE_SLAB)
                    || isOf(POLISHED_DIORITE_SLAB)
                    || isOf(DIORITE_STAIRS)
                    || isOf(POLISHED_DIORITE_STAIRS)
                    || isIn(BlockTags.STONE_BRICKS)
            ) blockHardness = 2.4f;
            else if (isOf(COBBLESTONE)) blockHardness = 2.0f;
            else if (isOf(COAL_ORE)) blockHardness = 1.2f;
            else if (isOf(GOLD_ORE) || isOf(NETHER_GOLD_ORE)) blockHardness = 2.4f;
            else if (isOf(LAPIS_ORE)
                    || isOf(IRON_ORE)
                    || isOf(DIAMOND_ORE)
                    || isOf(REDSTONE_ORE)
                    || isOf(EMERALD_ORE)
                    || isOf(NETHER_QUARTZ_ORE)
            ) blockHardness = 3.0f;
            else if (isOf(COPPER_ORE) || isOf(SILVER_ORE)) blockHardness = 2.5f;
            else if (isOf(MITHRIL_ORE)) blockHardness = 3.5f;
            else if (isOf(ADAMANTIUM_ORE)) blockHardness = 4.0f;

            else if (isOf(DEEPSLATE)) blockHardness = 3.6f;
            else if (isOf(COBBLED_DEEPSLATE)) blockHardness = 2.5f;
            else if (isOf(DEEPSLATE_COAL_ORE)) blockHardness = 1.8f;
            else if (isOf(DEEPSLATE_GOLD_ORE)) blockHardness = 3.6f;
            else if (isOf(DEEPSLATE_IRON_ORE)
                    || isOf(DEEPSLATE_LAPIS_ORE)
                    || isOf(DEEPSLATE_DIAMOND_ORE)
                    || isOf(DEEPSLATE_REDSTONE_ORE)
                    || isOf(DEEPSLATE_EMERALD_ORE)
            ) blockHardness = 4.5f;
            else if (isOf(DEEPSLATE_COPPER_ORE) || isOf(DEEPSLATE_SILVER_ORE)) blockHardness = 3.75f;
            else if (isOf(DEEPSLATE_MITHRIL_ORE)) blockHardness = 5.25f;
            else if (isOf(DEEPSLATE_ADAMANTIUM_ORE)) blockHardness = 6.0f;
            else if (isOf(TUFF)) blockHardness = 1.5f;
            else if (isOf(ANCIENT_DEBRIS)) blockHardness = 3.0f;
            else if (isOf(OBSIDIAN) || isOf(CRYING_OBSIDIAN)) blockHardness = 2.4f;

            else if (isOf(LAPIS_BLOCK)) blockHardness = 3.0f;
            else if (isOf(GOLD_BLOCK)) blockHardness = 4.8f;
            else if (isOf(IRON_BLOCK)) blockHardness = 9.6f;
            else if (isOf(DIAMOND_BLOCK)) blockHardness = 19.2f;
            else if (isOf(EMERALD_BLOCK)) blockHardness = 9.6f;
            else if (isOf(REDSTONE_BLOCK)) blockHardness = 5.0f;
            else if (isOf(COAL_BLOCK)) blockHardness = 1.2f;
            else if (isOf(COPPER_BLOCK) || isOf(SILVER_BLOCK)) blockHardness = 4.8f;
            else if (isOf(MITHRIL_BLOCK)) blockHardness = 76.8f;
            else if (isOf(TUNGSTEN_BLOCK)) blockHardness = 153.6f;
            else if (isOf(NETHERITE_BLOCK)) blockHardness = 153.6f;
            else if (isOf(ADAMANTIUM_BLOCK)) blockHardness = 307.2f;
            else if (isOf(ANCIENT_METAL_BLOCK)) blockHardness = 19.2f;

            else if (isOf(IRON_DOOR)) blockHardness = 4.0f;

            else if (isIn(BlockTags.BUTTONS)) blockHardness = 0.5f;
            else if (isIn(BlockTags.WOOL)) blockHardness = 0.8f;
            else if (isIn(BlockTags.PLANKS)) blockHardness = 0.8f;
            else if (isIn(BlockTags.LOGS)) blockHardness = 1.2f;
            else if (isIn(BlockTags.LEAVES)) blockHardness = 0.2f;
            else if (isIn(BlockTags.ICE)) blockHardness = 1.0f;
            else if (isIn(BlockTags.WOODEN_DOORS)) blockHardness = 0.25f;
            else if (isIn(BlockTags.SAND)) blockHardness = 0.4f;
            else if (isIn(BlockTags.BEDS)) blockHardness = 0.2f;
            else if (isIn(BlockTags.WOODEN_STAIRS)) blockHardness = 0.8f;
            else if (isIn(BlockTags.WOODEN_SLABS)) blockHardness = 0.4f;
            else if (isIn(BlockTags.CROPS)) blockHardness = 0.02f;
            else if (isIn(BlockTags.PRESSURE_PLATES)) blockHardness = 0.5f;
            else if (isIn(BlockTags.WOODEN_FENCES)) blockHardness = 0.4f;
            else if (isIn(BlockTags.WOODEN_TRAPDOORS)) blockHardness = 0.8f;

            else if (getBlock() instanceof StainedGlassBlock || getBlock() instanceof GlassBlock) blockHardness = 2.0f;
            else if (getBlock() instanceof StainedGlassPaneBlock) blockHardness = 0.1f;
            // cls_111: RunestoneBlock
            else if (getBlock() instanceof cls_111) blockHardness = 2.4f;
            else if (getBlock() instanceof InfestedBlock) blockHardness = 0.75f;

            // Portable block below
            else if (isOf(BLAST_FURNACE)) blockHardness = 2.0f;
            else if (isOf(CHEST)) blockHardness = 0.2f;
            else if (isOf(SMOKER)) blockHardness = 2.0f;
            else if (isOf(FLINT_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(OBSIDIAN_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(COPPER_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(SILVER_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(IRON_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(GOLD_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(ANCIENT_METAL_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(MITHRIL_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(TUNGSTEN_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(ADAMANTIUM_CRAFTING_TABLE)) blockHardness = 1.6f;
            else if (isOf(CLAY_FURNACE)) blockHardness = 0.5f;
            else if (isOf(HARDENED_CLAY_FURNACE)) blockHardness = 1.0f;
            else if (isOf(COBBLESTONE_FURNACE)) blockHardness = 2.0f;
            else if (isOf(NETHERRACK_FURNACE)) blockHardness = 8.0f;
            else if (isOf(OBSIDIAN_FURNACE)) blockHardness = 4.0f;
            else if (isOf(SANDSTONE_FURNACE)) blockHardness = 1.0f;
            else if (isOf(COPPER_ANVIL)) blockHardness = 4.8f;
            else if (isOf(SILVER_ANVIL)) blockHardness = 4.8f;
            else if (isOf(GOLD_ANVIL)) blockHardness = 4.8f;
            else if (isOf(IRON_ANVIL)) blockHardness = 9.6f;
            else if (isOf(ANCIENT_METAL_ANVIL)) blockHardness = 19.2f;
            else if (isOf(MITHRIL_ANVIL)) blockHardness = 76.8f;
            else if (isOf(TUNGSTEN_ANVIL)) blockHardness = 153.6f;
            else if (isOf(ADAMANTIUM_ANVIL)) blockHardness = 307.2f;
            else if (isOf(CHIPPED_COPPER_ANVIL)) blockHardness = 4.8f;
            else if (isOf(CHIPPED_SILVER_ANVIL)) blockHardness = 4.8f;
            else if (isOf(CHIPPED_GOLD_ANVIL)) blockHardness = 4.8f;
            else if (isOf(CHIPPED_IRON_ANVIL)) blockHardness = 9.6f;
            else if (isOf(CHIPPED_ANCIENT_METAL_ANVIL)) blockHardness = 19.2f;
            else if (isOf(CHIPPED_MITHRIL_ANVIL)) blockHardness = 76.8f;
            else if (isOf(CHIPPED_TUNGSTEN_ANVIL)) blockHardness = 153.6f;
            else if (isOf(CHIPPED_ADAMANTIUM_ANVIL)) blockHardness = 307.2f;
            else if (isOf(DAMAGED_COPPER_ANVIL)) blockHardness = 4.8f;
            else if (isOf(DAMAGED_SILVER_ANVIL)) blockHardness = 4.8f;
            else if (isOf(DAMAGED_GOLD_ANVIL)) blockHardness = 4.8f;
            else if (isOf(DAMAGED_IRON_ANVIL)) blockHardness = 9.6f;
            else if (isOf(DAMAGED_ANCIENT_METAL_ANVIL)) blockHardness = 19.2f;
            else if (isOf(DAMAGED_MITHRIL_ANVIL)) blockHardness = 76.8f;
            else if (isOf(DAMAGED_TUNGSTEN_ANVIL)) blockHardness = 153.6f;
            else if (isOf(DAMAGED_ADAMANTIUM_ANVIL)) blockHardness = 307.2f;
            else if (isOf(ENCHANTING_TABLE)) blockHardness = 2.4f;
            else if (isOf(EMERALD_ENCHANTING_TABLE)) blockHardness = 2.4f;

            else blockHardness = this.hardness;

            cir.setReturnValue(blockHardness);
        }

        @Inject(method = "isToolRequired", at = @At(value = "HEAD"), cancellable = true)
        private void isToolRequired(CallbackInfoReturnable<Boolean> cir) {
            if (isIn(BlockTags.ICE)) cir.setReturnValue(true);
            if (isOf(STONE_BUTTON)
                    || isOf(POLISHED_BLACKSTONE_BUTTON)
            )
                cir.setReturnValue(true);
            if (getBlock() instanceof AbstractGlassBlock) cir.setReturnValue(true);
            if (getBlock() instanceof StainedGlassPaneBlock) cir.setReturnValue(true);
            if (isOf(BEDROCK) || isOf(MANTLE)) cir.setReturnValue(true);
        }
    }
}
