package com.antihero.enhancedmite.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {MiningToolItem.class})
public abstract class MiningToolItemMixin extends ToolItem {
    @Shadow @Final private TagKey<Block> effectiveBlocks;

    @Shadow @Final protected float miningSpeed;

    public MiningToolItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ToolMaterial;getMiningSpeedMultiplier()F"))
    private float constructor(ToolMaterial instance) {
        if (instance == ToolMaterials.WOOD) return 3.0f;
        else if (instance == ToolMaterials.FLINT) return 3.0f;
        else if (instance == ToolMaterials.COPPER || instance == ToolMaterials.SILVER || instance == ToolMaterials.RUSTED_IRON) return 4.0f;
        else if (instance == ToolMaterials.IRON || instance == ToolMaterials.ANCIENT_METAL) return 6.0f;
        else if (instance == ToolMaterials.GOLD) return 12.0f;
        else if (instance == ToolMaterials.MITHRIL) return 8.0f;
        else if (instance == ToolMaterials.TUNGSTEN || instance == ToolMaterials.NETHERITE) return 9.0f;
        else if (instance == ToolMaterials.ADAMANTIUM) return 10.0f;
        else return instance.getMiningSpeedMultiplier();
    }

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    public float getMiningSpeedMultiplier(ItemStack stack, @NotNull BlockState state) {
        return state.isIn(this.effectiveBlocks) ? this.miningSpeed : 1.0f;
    }

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    public boolean postMine(ItemStack stack, @NotNull World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && state.getHardness(world, pos) != 0.0f) {
            stack.damage((int) (state.getHardness(world, pos) * 100), miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }
}
