package cxy.cxybalanceddiet.netWork.packet;

import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.netWork.NetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientReceiver4S {

    public static void receiver() {
        ClientPlayNetworking.registerGlobalReceiver(
                NetworkHandler.THIRST_VALUE,
                (client, handler, buf, responseSender) -> {

                    // 在客户端主线程上执行操作
                    client.execute(() -> {

                        // 根据接收到的温度数据更新客户端，比如HUD
                        if (client.player != null) {
                            Accessor accessor = (Accessor) client.player;
                            accessor.getThirstManager().readFromData(buf);

                        }
                    });


                }
        );
    }
}
