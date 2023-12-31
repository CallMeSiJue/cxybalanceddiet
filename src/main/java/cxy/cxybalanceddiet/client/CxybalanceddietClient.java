package cxy.cxybalanceddiet.client;

import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.attribute.TempManager;
import cxy.cxybalanceddiet.netWork.packet.ClientReceiver4S;
import cxy.cxybalanceddiet.netWork.packet.ClientSender2S;
import cxy.cxybalanceddiet.render.RenderRigistry;
import cxy.cxybalanceddiet.utils.TemHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;

public class CxybalanceddietClient implements ClientModInitializer {
    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        RenderRigistry.register();
        ClientReceiver4S.receiver();
        // 注册监听器，以便在每个tick执行
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            // 数据处理
            if (client.world != null) {
                ClientPlayerEntity player = client.player;
                if (player == null) {
                    return;
                }

                Accessor accessor = (Accessor) player;
                TempManager tempManager = accessor.getTempManager();
                if (!player.isCreative() && !player.isSpectator()) {
                    TemHandler.checkCoolTick(tempManager, client.player);
                    TemHandler.checkHotTick(tempManager, client.player);
                }
                tickCounter++;
                if (tickCounter >= 20) {
                    boolean paused = client.isInSingleplayer() && client.isPaused();
                    if (!paused && !player.isCreative() && !player.isSpectator()) {
                        ClientSender2S.sender(player);
                    }
                    tickCounter = 0; // 重置计数器

                }
            }

        });
    }
}
