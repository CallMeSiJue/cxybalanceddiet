package cxy.cxybalanceddiet.netWork.packet;

import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.netWork.NetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSender2C {
    private static final Logger log = LoggerFactory.getLogger(ServerSender2C.class);

    public static void sender(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        
        PacketByteBuf thirstData = PacketByteBufs.create();
        Accessor accessor = (Accessor) player;
        NetworkHandler.writeNutritionValue(thirstData, accessor);

        ServerPlayNetworking.send(player, NetworkHandler.NUTRITION_VALUE, thirstData);
    }
}
