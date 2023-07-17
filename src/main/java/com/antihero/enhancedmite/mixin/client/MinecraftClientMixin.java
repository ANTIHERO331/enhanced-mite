package com.antihero.enhancedmite.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {MinecraftClient.class})
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;
    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow private int itemUseCooldown;

    @Redirect(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/ParticleManager;addBlockBreakingParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V"))
    private void addBlockBreakingParticles(ParticleManager instance, BlockPos pos, Direction direction) {
        assert player != null;
        assert world != null;
        if (world.getBlockState(pos).getHardness(world, pos) == -1) return;
        if (player.canHarvest(world.getBlockState(pos))) instance.addBlockBreakingParticles(pos, direction);
    }

    @Inject(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isRiding()Z"))
    private void onItemUseCooldownChanged(CallbackInfo ci) {
        assert player != null;
        if (player.getMainHandStack().isFood()) this.itemUseCooldown = 20;
        else this.itemUseCooldown = 8;
    }

    @Redirect(method = "handleInputEvents",
            slice = @Slice(id = "slice_1", from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/option/GameOptions;useKey:Lnet/minecraft/client/option/KeyBinding;",
                    ordinal = 2)
            ),
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/option/KeyBinding;wasPressed()Z",
                    ordinal = 0
            )
    )
    private boolean wasPressed(KeyBinding instance) {
        return instance.wasPressed() && this.itemUseCooldown == 0 && this.player != null && !this.player.isUsingItem();
    }
}
