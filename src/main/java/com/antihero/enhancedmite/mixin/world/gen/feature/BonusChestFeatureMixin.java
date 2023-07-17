package com.antihero.enhancedmite.mixin.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.BonusChestFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BonusChestFeature.class)
public abstract class BonusChestFeatureMixin extends Feature<DefaultFeatureConfig> {
    public BonusChestFeatureMixin(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Redirect(method = "generate", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1)
    )
    private boolean setBlockState(StructureWorldAccess instance, BlockPos pos, BlockState state, int i) {
        return false;
    }
}
