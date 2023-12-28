package cxy.cxybalanceddiet.render;

import com.mojang.blaze3d.systems.RenderSystem;
import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.attribute.Accessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class ThirstRender {
    private static final Identifier THIRST_HUD = new Identifier(Cxybalanceddiet.MOD_ID, "textures/thirst/thirst_icons.png");

    public static void renderThird(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();


        ClientPlayerEntity player = client.player;

        if (player == null || player.isCreative() || player.isSpectator()) {
            return;
        }
        Accessor playerAccessor = (Accessor) player;
        int width = client.getWindow().getScaledWidth() / 2;
        int height = client.getWindow().getScaledHeight();
        int bounceFactor = 0;

        // Defining the texture
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, THIRST_HUD);

        // Get the ThirstManager for the player


        // Get the thirstValue
        int thirstValue = (int) playerAccessor.getThirstManager().getValue();

        // If the player currently is in a hot biome for enough time or in The Nether
        int hotXFactor = 0;
        int hotYFactor = 0;

//        if (playerData.playerTempStatus == PlayerTempStatus.HOT.getCode()) {
//            hotYFactor = 9;
//        } else if (playerData.playerTempStatus == PlayerTempStatus.VERY_HOT.getCode()) {
//            hotXFactor = 36;
//        }


        // If the player currently has the thirst effect


        // Create the Thirst Bar
        // Empty Thirst
        for (int i = 0; i < 10; i++) {

            drawContext.drawTexture(THIRST_HUD,
                    (width + 82 - (i * 9) + i),
                    (height - 49 + bounceFactor),
                    hotYFactor,
                    0,
                    9,
                    9,
                    256,
                    256);
        }

        // Half Thirst
        for (int i = 0; i < 20; i++) {
            if (thirstValue != 0) {
                if (((thirstValue + 1) / 2) > i) {

                    drawContext.drawTexture(THIRST_HUD,
                            (width + 82 - (i * 9) + i),
                            (height - 49 + bounceFactor),
                            9 + hotXFactor,
                            9,
                            9,
                            9,
                            256,
                            256);
                } else {
                    break;
                }
            }
        }

        // Full Thirst
        for (int i = 0; i < 20; i++) {
            if (thirstValue != 0) {
                if ((thirstValue / 2) > i) {

                    drawContext.drawTexture(THIRST_HUD,
                            (width + 82 - (i * 9) + i),
                            (height - 49 + bounceFactor),
                            hotXFactor,
                            9,
                            9,
                            9,
                            256,
                            256);
                } else {
                    break;
                }
            }
        }


    }


}
