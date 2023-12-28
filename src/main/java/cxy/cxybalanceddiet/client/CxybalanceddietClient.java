package cxy.cxybalanceddiet.client;

import cxy.cxybalanceddiet.netWork.packet.ClientReceiver4S;
import cxy.cxybalanceddiet.netWork.packet.ClientSender2S;
import cxy.cxybalanceddiet.render.RenderRigistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
                if (client.player == null) {
                    return;
                }
                tickCounter++;
                if (tickCounter >= 20) {
                    boolean paused = client.isInSingleplayer() && client.isPaused();
                    if (!paused && !client.player.isCreative() && !client.player.isSpectator()) {
                        ClientSender2S.sender(client.player);
                    }
                    tickCounter = 0; // 重置计数器

                }
            }

        });
    }
}
