package com.antihero.enhancedmite.mixin.client.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {ClientPlayerInteractionManager.class}, priority = 2000)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow private ItemStack selectedStack;

    @Shadow private BlockPos currentBreakingPos;

    @Redirect(method = "attackBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean attackBlock$isCurrentlyBreaking(ClientPlayerInteractionManager instance, BlockPos pos) {
        return isCurrentlyBreaking(pos);
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean updateBlockBreakingProgress$isCurrentlyBreaking(ClientPlayerInteractionManager instance, BlockPos pos) {
        return isCurrentlyBreaking(pos);
    }

    private boolean isCurrentlyBreaking(BlockPos pos) {
        assert this.client.player != null;
        ItemStack itemStack = this.client.player.getMainHandStack();
        boolean bl = this.selectedStack.isEmpty() && itemStack.isEmpty();

        if (!selectedStack.isDamageable() && !itemStack.isDamageable())
            return pos.equals(currentBreakingPos);

        else if (!this.selectedStack.isEmpty() && !itemStack.isEmpty())
            bl = selectedStack.getItem().getClass() == itemStack.getItem().getClass();

        return pos.equals(this.currentBreakingPos) && bl;
    }
}
