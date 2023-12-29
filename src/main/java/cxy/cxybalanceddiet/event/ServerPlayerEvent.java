package cxy.cxybalanceddiet.event;

import cxy.cxybalanceddiet.attribute.*;
import cxy.cxybalanceddiet.netWork.NetworkHandler;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.network.PacketByteBuf;

public class ServerPlayerEvent {
    public static void init() {
        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
            if (oldPlayer == null || newPlayer == null) {
                return;
            }
            HungerManager oldHungerManager = oldPlayer.getHungerManager();
            ThirstManager oldThirstManager = ((Accessor) oldPlayer).getThirstManager();
            FatManager fatManager = ((Accessor) oldPlayer).getFatManager();
            FiberManager fiberManager = ((Accessor) oldPlayer).getFiberManager();
            ProteinManager proteinManager = ((Accessor) oldPlayer).getProteinManager();

            HungerManager newHungerManager = newPlayer.getHungerManager();
            ThirstManager newThirstManager = ((Accessor) newPlayer).getThirstManager();
            FatManager newFatManager = ((Accessor) newPlayer).getFatManager();
            FiberManager newFiberManager = ((Accessor) newPlayer).getFiberManager();
            ProteinManager newProteinManager = ((Accessor) newPlayer).getProteinManager();
            if (!alive) {
                newHungerManager.setSaturationLevel(1.0F);
                oldThirstManager.copyFrom(oldThirstManager);
                newFatManager.copyFrom(fatManager);
                newFiberManager.copyFrom(fiberManager);
                newProteinManager.copyFrom(proteinManager);

            } else {
                newHungerManager.setFoodLevel(oldHungerManager.getFoodLevel());
                newThirstManager.setValue(oldThirstManager.getValue());

            }

        }));
        // 玩家加入时发送数据包
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

            PacketByteBuf data = PacketByteBufs.create();

            server.execute(() -> {
                Accessor accessor = (Accessor) handler.player;
                NetworkHandler.writeNutritionValue(data, accessor);
                ServerPlayNetworking.send(handler.getPlayer(), NetworkHandler.NUTRITION_VALUE, data);
            });

        });


    }
}
