package com.antihero.enhancedmite.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = {BlockItem.class})
public abstract class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    protected boolean place(ItemPlacementContext context, BlockState state) {
        return context.getWorld().setBlockState(context.getBlockPos(), state, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
    }
}
