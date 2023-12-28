package cxy.cxybalanceddiet.netWork;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.netWork.packet.ServerSender2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkHandler {
    /**
     * 口渴传输
     */
    public static final Identifier THIRST_VALUE = new Identifier(Cxybalanceddiet.MOD_ID, "thirst_value");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(THIRST_VALUE, ServerSender2C::sender);
    }
}
