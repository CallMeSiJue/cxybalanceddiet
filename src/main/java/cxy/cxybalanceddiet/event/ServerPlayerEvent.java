package cxy.cxybalanceddiet.event;

import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.attribute.ThirstManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.player.HungerManager;

public class ServerPlayerEvent {
    public static void init() {
        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) -> {
            if (oldPlayer == null || newPlayer == null) {
                return;
            }
            HungerManager oldHungerManager = oldPlayer.getHungerManager();
            ThirstManager oldThirstManager = ((Accessor) oldPlayer).getThirstManager();

            HungerManager newHungerManager = newPlayer.getHungerManager();
            ThirstManager newThirstManager = ((Accessor) newPlayer).getThirstManager();
            if (!alive) {
                newHungerManager.setSaturationLevel(1.0F);
                newThirstManager.reset();

            } else {
                newHungerManager.setFoodLevel(oldHungerManager.getFoodLevel());
                newThirstManager.setValue(oldThirstManager.getValue());

            }

        }));
//        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//
//            PacketByteBuf data = PacketByteBufs.create();
//            if (handler.getPlayer() != null && handler.getPlayer().age < 20) {
//                server.execute(() -> {
//                    new ThirstManager().writeToData(data);
//                    ServerPlayNetworking.send(handler.getPlayer(), NetworkHandler.THIRST_VALUE, data);
//                });
//            }
//        });


    }
}
