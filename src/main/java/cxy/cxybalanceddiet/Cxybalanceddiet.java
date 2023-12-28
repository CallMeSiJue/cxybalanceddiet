package cxy.cxybalanceddiet;

import cxy.cxybalanceddiet.event.ServerPlayerEvent;
import cxy.cxybalanceddiet.netWork.NetworkHandler;
import net.fabricmc.api.ModInitializer;

public class Cxybalanceddiet implements ModInitializer {
    public static final String MOD_ID = "cxybalanceddiet";

    @Override
    public void onInitialize() {
        ServerPlayerEvent.init();
        NetworkHandler.registerC2SPackets();
    }
}
