package cxy.cxybalanceddiet.netWork;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.netWork.packet.ServerSender2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class NetworkHandler {
    /**
     * 口渴传输
     */
    public static final Identifier TICK_VALUE = new Identifier(Cxybalanceddiet.MOD_ID, "tick_value");
    public static final Identifier NUTRITION_VALUE = new Identifier(Cxybalanceddiet.MOD_ID, "nutrition_value");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TICK_VALUE, ServerSender2C::sender);
    }

    public static void readFromClient(PacketByteBuf data, Accessor accessor) {

        accessor.getTempManager().readFromClient(data);

    }

    public static void writeToServer(PacketByteBuf data, Accessor accessor) {

        accessor.getTempManager().writeToServer(data);
    }

    public static void readNutritionValue(PacketByteBuf data, Accessor accessor) {
        accessor.getThirstManager().readFromData(data);
        accessor.getFatManager().readFromData(data);
        accessor.getFiberManager().readFromData(data);
        accessor.getProteinManager().readFromData(data);
        accessor.getTempManager().readFromData(data);

    }

    public static void writeNutritionValue(PacketByteBuf data, Accessor accessor) {
        accessor.getThirstManager().writeToData(data);
        accessor.getFatManager().writeToData(data);
        accessor.getFiberManager().writeToData(data);
        accessor.getProteinManager().writeToData(data);
        accessor.getTempManager().writeToData(data);
    }

}
