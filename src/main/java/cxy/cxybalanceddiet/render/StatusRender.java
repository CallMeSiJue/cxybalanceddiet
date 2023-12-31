package cxy.cxybalanceddiet.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.attribute.TempManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class StatusRender {
    private static final Identifier HUD = new Identifier(Cxybalanceddiet.MOD_ID, "textures/gui/hcs_stat.png");

    public static void statusRender(DrawContext drawContext, float tickDelta) {
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
        // temp
        TempManager tempManager = playerAccessor.getTempManager();
        int temp = (int) tempManager.getValue();
        double envTemp = tempManager.getEnvTemp();
        Integer icon = getIcon(envTemp);
        drawContext.drawTexture(HUD, x - 18, height, 16 * icon, 64, 16, 16);
        drawContext.drawTextWithShadow(textRenderer, String.valueOf(temp), x - 18, height + 17, 0xFFFFFF);
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

    private static Integer getIcon(Double envTemp) {
        if (envTemp < -26) {
            return 2;
        } else if (envTemp < -16) {
            return 3;
        } else if (envTemp < -6) {
            return 4;
        } else if (envTemp < 6) {
            return 5;
        } else if (envTemp < 16) {
            return 6;
        } else if (envTemp < 36) {
            return 7;
        } else if (envTemp < 46) {
            return 8;
        } else if (envTemp < 56) {
            return 9;
        } else if (envTemp < 66) {
            return 10;
        } else if (envTemp < 76) {
            return 11;
        } else {
            return 12;
        }

    }


}
