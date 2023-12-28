package cxy.cxybalanceddiet.netWork.packet;

import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.netWork.NetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

public class ClientSender2S {
    public static void sender(PlayerEntity player) {
        PacketByteBuf thirstData = PacketByteBufs.create();
        Accessor accessor = (Accessor) player;
        accessor.getThirstManager().writeToData(thirstData);
        ClientPlayNetworking.send(NetworkHandler.THIRST_VALUE, thirstData);
    }
}
