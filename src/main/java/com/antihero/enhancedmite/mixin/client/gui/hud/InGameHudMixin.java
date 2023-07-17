package com.antihero.enhancedmite.mixin.client.gui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {InGameHud.class})
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow @Final private MinecraftClient client;

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow public abstract TextRenderer getTextRenderer();

    /**
     * @author Antihero
     * @reason None
     */
    @Overwrite
    public void renderExperienceBar(MatrixStack matrices, int x) {
        assert client.player != null;

        client.getProfiler().push("expBar");
        RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
        int i = client.player.getNextLevelExperience();
        if (i > 0) {
            int k = (int)(client.player.experienceProgress * 183.0f);
            int l = scaledHeight - 32 + 3;
            drawTexture(matrices, x, l, 0, 64, 182, 5);
            if (k > 0) drawTexture(matrices, x, l, 0, 69, k, 5);
        }
        this.client.getProfiler().pop();

        int color = 11546150;
        if (this.client.player.experienceLevel >= 0) color = 8453920;

        client.getProfiler().push("expLevel");
        String s = "" + this.client.player.experienceLevel;
        int i1 = (this.scaledWidth - this.getTextRenderer().getWidth(s)) / 2;
        int j1 = this.scaledHeight - 31 - 4;

        if (client.player.experienceLevel != 0) {
            this.getTextRenderer().draw(matrices, s, i1 + 1, j1, 0);
            this.getTextRenderer().draw(matrices, s, i1 - 1, j1, 0);
            this.getTextRenderer().draw(matrices, s, i1, j1 + 1, 0);
            this.getTextRenderer().draw(matrices, s, i1, j1 - 1, 0);
            this.getTextRenderer().draw(matrices, s, i1, j1, color);
        }

        this.client.getProfiler().pop();
    }
}
