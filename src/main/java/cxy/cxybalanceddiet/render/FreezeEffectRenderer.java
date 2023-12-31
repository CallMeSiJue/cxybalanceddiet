package cxy.cxybalanceddiet.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.attribute.TempManager;
import cxy.cxybalanceddiet.utils.PlayerStatusManage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FreezeEffectRenderer {
    private static final Identifier FREEZE_TEXTURE = new Identifier(Cxybalanceddiet.MOD_ID, "textures/gui/freeze.png");


    public static void renderFreezeEffect(DrawContext drawContext, float tickDelta) {

        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player == null || mc.player.isCreative() || mc.player.isSpectator()) {
            return;
        }

        Accessor accessor = (Accessor) mc.player;
        TempManager tempManager = accessor.getTempManager();
        if (tempManager.getColdTick() > 0) {
            float freezePercent = PlayerStatusManage.getFreezePercent(tempManager.getColdTick());
            int scaledWidth = mc.getWindow().getScaledWidth();
            int scaledHeight = mc.getWindow().getScaledHeight();
            renderOverlay(drawContext, FREEZE_TEXTURE, freezePercent, scaledWidth, scaledHeight);
        }


    }

    public static void renderOverlay(DrawContext context, Identifier texture, float opacity, int x1, int x2) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        context.drawTexture(texture, 0, 0, -90, 0.0f, 0.0f, x1, x2, x1, x2);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }


}
