package com.antihero.enhancedmite.mixin.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = {SwordItem.class})
public abstract class SwordItemMixin extends ToolItem implements Vanishable {
    public SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        if (state.isOf(Blocks.COBWEB) || material == Material.REPLACEABLE_PLANT || material == Material.PLANT) {
            return 15.0f;
        }
        if (state.isIn(BlockTags.LEAVES) || material == Material.GOURD) {
            return 1.5f;
        }
        return 1.0f;
    }
}
