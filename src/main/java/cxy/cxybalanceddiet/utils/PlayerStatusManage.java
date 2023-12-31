package cxy.cxybalanceddiet.utils;


import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.attribute.TempManager;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * 建议服务端调用
 */
public class PlayerStatusManage {

    private static final Logger log = LoggerFactory.getLogger(PlayerStatusManage.class);
    private static final Identifier FREEZE_TEXTURE = new Identifier(Cxybalanceddiet.MOD_ID, "textures/gui/freeze.png");
    private static final UUID POWDER_SNOW_SLOW_ID = UUID.fromString("1eaf83ff-7207-4526-b37a-d7a07b3ec4ce");

    public static void reduceHunger(PlayerEntity player, double amount) {
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel((int) (hungerManager.getFoodLevel() - amount));
        // 注意检查饥饿度不要低于0
        if (hungerManager.getFoodLevel() < 0) {
            hungerManager.setFoodLevel(0);
        }
    }

    public static void coldDamageIfNeed(ServerPlayerEntity player, TempManager tempManager) {
        if (tempManager.getColdTick() >= 140) {
            player.damage(player.getWorld().getDamageSources().freeze(), 0.5f);

        }
    }

    public static void hotDamageIfNeed(ServerPlayerEntity player, TempManager tempManager) {
        if (tempManager.hotTick >= 380) {
            player.damage(player.getWorld().getDamageSources().hotFloor(), 0.5f);
//            s
        }
    }

    public static boolean hasHireResistance(ServerPlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }


    public static void addPowderSnowSlowIfNeeded(PlayerEntity player, TempManager tempManager) {
        int i;
        if (tempManager.getColdTick() > 0) {
            EntityAttributeInstance entityAttributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (entityAttributeInstance == null) {
                return;
            }
            float f = -0.05f * getFreezePercent(tempManager.getColdTick());
            entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(POWDER_SNOW_SLOW_ID, "Powder snow slow", f, EntityAttributeModifier.Operation.ADDITION));
        }
    }

    public static float getFreezePercent(int freezeCount) {
        if (freezeCount > 200) {
            freezeCount = 200;
        }

        // 实现获取冻结百分比的逻辑
        // 例如：return player.getTicksFrozen() / (float) player.getMaxFreezeTime();
        return freezeCount / 200f;
    }

    public static void removePowderSnowSlow(PlayerEntity player) {
        EntityAttributeInstance entityAttributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (entityAttributeInstance == null) {
            return;
        }
        if (entityAttributeInstance.getModifier(POWDER_SNOW_SLOW_ID) != null) {
            entityAttributeInstance.removeModifier(POWDER_SNOW_SLOW_ID);
        }
    }


    public static void reduceHungryIfNeed(PlayerEntity player, TempManager tempManager) {
        if (tempManager.getTempStatus(player) == PlayerTempStatus.COOL.getCode()) {
            player.getHungerManager().addExhaustion(0.1f);
        } else if (tempManager.getTempStatus(player) == PlayerTempStatus.VERY_COOL.getCode()) {
            player.getHungerManager().addExhaustion(0.2f);
        }
    }


    public static void applyNauseaEffectIfNeed(PlayerEntity player, TempManager tempManager) {
        if (tempManager.getValue() > 10 && tempManager.getTempStatus(player) < 1) {
            return;
        }
        int duration = 200; // 持续时间，以tick为单位，20 tick = 1秒
        int amplifier = tempManager.getTempStatus(player) - 1; // 效果等级，1 表示 Nausea II

        StatusEffectInstance nausea = new StatusEffectInstance(StatusEffects.NAUSEA, duration, amplifier);
        player.addStatusEffect(nausea);
    }
}
