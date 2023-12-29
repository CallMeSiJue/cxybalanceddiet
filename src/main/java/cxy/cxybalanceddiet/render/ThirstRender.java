package cxy.cxybalanceddiet.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.attribute.Accessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class ThirstRender {
    private static final Identifier HUD = new Identifier(Cxybalanceddiet.MOD_ID, "textures/gui/hcs_stat.png");

    public static void renderThird(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();


        ClientPlayerEntity player = client.player;

        if (player == null || player.isCreative() || player.isSpectator()) {
            return;
        }
        Accessor playerAccessor = (Accessor) player;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight() / 2;


        // Defining the texture
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, HUD);


        // Get the thirstValue
        int thirstValue = (int) playerAccessor.getThirstManager().getValue();
        int x = width - 5 - 16;
        int thiHeight = (int) ((thirstValue / 100d) * 16);
        TextRenderer textRenderer = client.textRenderer;

        drawContext.drawTexture(HUD, x, height, 0, 48, 16, 16);
        drawContext.drawTexture(HUD, x, height + (16 - thiHeight), 16, 48 + (16 - thiHeight), 16, thiHeight);
        drawContext.drawTextWithShadow(textRenderer, String.valueOf(thirstValue), x, height + 17, 0xFFFFFF);

        // FAT
        height += 28;
        int farValue = (int) playerAccessor.getFatManager().getValue();
        drawContext.drawTexture(HUD, x, height, 0, 144, 16, 16);
        drawContext.drawTextWithShadow(textRenderer, String.valueOf(farValue), x, height + 17, 0xFFFFFF);
        // FIBER
        height += 28;
        int fiberValue = (int) playerAccessor.getFiberManager().getValue();
        drawContext.drawTexture(HUD, x, height, 32, 144, 16, 16);
        drawContext.drawTextWithShadow(textRenderer, String.valueOf(fiberValue), x, height + 17, 0xFFFFFF);

        // PROTEIN
        height += 28;
        int proteinValue = (int) playerAccessor.getProteinManager().getValue();
        drawContext.drawTexture(HUD, x, height, 16, 144, 16, 16);
        drawContext.drawTextWithShadow(textRenderer, String.valueOf(proteinValue), x, height + 17, 0xFFFFFF);
    }


}
