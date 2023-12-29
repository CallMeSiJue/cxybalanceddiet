package cxy.cxybalanceddiet.utils;

import com.google.common.util.concurrent.AtomicDouble;
import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.attribute.FatManager;
import cxy.cxybalanceddiet.attribute.TempManager;
import cxy.cxybalanceddiet.config.CustomBiomeTemperatures;
import cxy.cxybalanceddiet.config.TempConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 温度处理类
 */
public class TemHandler {
    /**
     * 基本高度温度
     */
    public static final double NORMAL_TEM = 26;
    /**
     * 没增加一格减少的温度
     */
    public static final double TEM_EACH_LEVEL = 0.0625;

    /**
     * 基本温度高度
     */
    public static final int BASE_HIGHT = 63;

    /**
     * 基本适应温度
     */
    public static final double BASIC_ADAPTATION_TEMP = 6;
    /**
     * 玩家的每秒产生的体温
     */
    public static final double BASIC_PLAYER_TEMP_RISE = 0.5;
    /**
     * 玩家的每秒无衣服流逝的体温
     */
    public static final double BASIC_PLAYER_TEMP_LOSE = -0.5;

    private static final Logger log = LoggerFactory.getLogger(TemHandler.class);

    public static Double getEnvironmentTemperature(MinecraftServer server, PlayerEntity player) {
        TempConfig tempConfig = TempConfig.getInstance();
        AtomicDouble reallyTemp = new AtomicDouble(getPlayerReallyTemp(player));

        Biome.Precipitation precip = player.getWorld().getBiome(player.getBlockPos()).value().getPrecipitation(player.getBlockPos());

        //下雪还是 下雨
        if (precip == Biome.Precipitation.RAIN) {
            if (player.getWorld().isRaining()) {
                if (player.isWet() && !player.isTouchingWater()) {
                    reallyTemp.addAndGet(-8);
                }
            }
        } else if (precip == Biome.Precipitation.SNOW) {
            if (player.getWorld().isRaining()) {
                reallyTemp.addAndGet(-16);
            }
        }


        // 判断加温 分块
        Vec3d pos = player.getPos();
        Stream<BlockState> heatBlockBox = player.getWorld().getStatesInBox(Box.of(pos, 4, 4, 4));
        heatBlockBox.forEach((state) -> {
            tempConfig.heatingBlocks.forEach((b, t) -> {
                if (Objects.equals(state.getBlock().toString(), b)) {

                    if (state.isOf(Blocks.CAMPFIRE) || state.isOf(Blocks.SOUL_CAMPFIRE)) { //hard code to keep unlit campfires from heating.
                        if (state.get(CampfireBlock.LIT)) {
                            reallyTemp.addAndGet(t);
                        }
                    } else {
                        reallyTemp.addAndGet(t);
                    }

                }
            });
        });

        // 判断 降温 分块
        Stream<BlockState> coldBlockBox = player.getWorld().getStatesInBox(Box.of(pos, 2, 3, 2));
        coldBlockBox.forEach((state) -> {
            tempConfig.coolingBlocks.forEach((b, t) -> {
                if (Objects.equals(state.getBlock().toString(), b)) {
                    reallyTemp.addAndGet(t);
                }
            });
        });

        // 判断 手持 温度
        tempConfig.heldTempItems.forEach((it, t) -> {
            if (Objects.equals(player.getInventory().getMainHandStack().getItem().toString(), it)) {
                reallyTemp.addAndGet(t);
            }
            if (Objects.equals(player.getInventory().offHand.get(0).getItem().toString(), it)) {
                reallyTemp.addAndGet(t);
            }
        });

        return reallyTemp.get();
    }


    public static void dealPlayerTemp(MinecraftServer server, PlayerEntity player) {
        double lose = player.isWet() ? 2 : 1;
        double rise = player.isSprinting() ? 2 : 1;

        Accessor accessor = (Accessor) player;
        TempManager tempManager = accessor.getTempManager();
        FatManager fatManager = accessor.getFatManager();
        double temp = tempManager.getValue();
        double lose2 = temp > NORMAL_TEM ? 1.2 : 1;
        double rise2 = temp < NORMAL_TEM ? 1.2 : 1;

        double tempLose = fatManager.getTempLose();
        Double environmentTemperature = getEnvironmentTemperature(server, player);
        double riseTemp = BASIC_PLAYER_TEMP_RISE * getPlayerRise(environmentTemperature, player) * rise * rise2;

        double loseTemp = BASIC_PLAYER_TEMP_LOSE * getPlayerLose(environmentTemperature, player) * lose * lose2 * tempLose;

        double result = temp + riseTemp + loseTemp;
        tempManager.setValue(result);
        tempManager.setEnvTemp(environmentTemperature);
    }

    /**
     * 计算玩家的 保温适应范围
     *
     * @param player
     * @return
     */
    public static double getPlayerAdaptionTemp(PlayerEntity player) {
        AtomicDouble adaptionTemp = new AtomicDouble();
        TempConfig tempConfig = TempConfig.getInstance();
        tempConfig.bootTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(0).getItem().toString(), t.getName())) {
                adaptionTemp.addAndGet(t.getAdaptTemperature());
            }
        });
        tempConfig.leggingTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(1).getItem().toString(), t.getName())) {
                adaptionTemp.addAndGet(t.getAdaptTemperature());
            }
        });
        tempConfig.chestplateTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(2).getItem().toString(), t.getName())) {
                adaptionTemp.addAndGet(t.getAdaptTemperature());
            }
        });
        tempConfig.helmetTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(3).getItem().toString(), t.getName())) {
                adaptionTemp.addAndGet(t.getAdaptTemperature());
            }
        });


        return adaptionTemp.get();
    }

    /**
     * 计算玩家的 保温变化系数
     *
     * @param player
     * @return
     */
    public static double getPlayerAdaption(PlayerEntity player) {
        AtomicDouble adaptionTemp = new AtomicDouble(1);
        TempConfig tempConfig = TempConfig.getInstance();
        tempConfig.bootTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(0).getItem().toString(), t.getName())) {
                adaptionTemp.set(adaptionTemp.get() * t.getAdaptTickAdd());
            }
        });
        tempConfig.leggingTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(1).getItem().toString(), t.getName())) {
                adaptionTemp.set(adaptionTemp.get() * t.getAdaptTickAdd());
            }
        });
        tempConfig.chestplateTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(2).getItem().toString(), t.getName())) {
                adaptionTemp.set(adaptionTemp.get() * t.getAdaptTickAdd());
            }
        });
        tempConfig.helmetTempItems.forEach((t) -> {
            if (Objects.equals(player.getInventory().getArmorStack(3).getItem().toString(), t.getName())) {
                adaptionTemp.set(adaptionTemp.get() * t.getAdaptTickAdd());
            }
        });

        return adaptionTemp.get();
    }

    public static int getTotalFireProtectionLevel(PlayerEntity player) {
        int totalFireProtectionLevel = 0;

        // 遍历玩家的盔甲栏
        for (ItemStack armorItem : player.getInventory().armor) {
            if (!armorItem.isEmpty() && armorItem.hasEnchantments()) {
                // 检查火焰保护附魔并累加其等级
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(armorItem);
                if (enchantments.containsKey(Enchantments.FIRE_PROTECTION)) {
                    totalFireProtectionLevel += enchantments.get(Enchantments.FIRE_PROTECTION);
                }
            }
        }

        return totalFireProtectionLevel;
    }

    public static double getPlayerBiomeTemperature(PlayerEntity player) {
        World world = player.getWorld(); // 获取玩家所在的世界
        BlockPos pos = player.getBlockPos(); // 获取玩家的位置
        Biome biome = world.getBiome(pos).value(); // 获取该位置的群系
        return CustomBiomeTemperatures.getBiomeTemperature(world, biome);
    }

    public static double getPlayerReallyTemp(PlayerEntity player) {
        double y = player.getPos().getY();
        double playerBiomeTemperature = getPlayerBiomeTemperature(player);

        World world = player.getWorld();

        double reallyBiomeTemp = 50 * playerBiomeTemperature - 7.5;
        double timeTempEffect = calculateTemperatureCoefficient(world.getTimeOfDay()) * 20 * Math.abs(playerBiomeTemperature);
        return reallyBiomeTemp - (y - BASE_HIGHT) * TEM_EACH_LEVEL + timeTempEffect;

    }

    /**
     * 计算时间温度系数
     *
     * @param timeOfDay
     * @return
     */
    public static double calculateTemperatureCoefficient(long timeOfDay) {

        if (timeOfDay > 24000) {
            timeOfDay = timeOfDay % 24000;
        }
        double period = 24000;
        double b = 2 * Math.PI / period;

        // 使波形在x=8000时处于2π（一个完整周期的起点）
        double c = timeOfDay - 8000;

        // 计算并返回余弦函数的值
        return Math.cos(c * b);


    }

    /**
     * 当外界温度 很低的时候，玩家产生热量的效率应该下降
     *
     * @param envTemp
     * @return
     */
    public static double getPlayerRise(double envTemp, PlayerEntity player) {
        final double lowerBoundTemp = -200; // 环境温度下限
        double minAdaptTemp = NORMAL_TEM - BASIC_ADAPTATION_TEMP - getPlayerAdaptionTemp(player); // 环境温度上限

        if (envTemp < lowerBoundTemp) {
            return 0; // 如果环境温度低于-200，效率为0
        } else if (envTemp > minAdaptTemp) {
            return 1; // 如果环境温度高于最小适应温度，效率为1
        } else {
            // 在两个极端之间线性插值
            return (envTemp - lowerBoundTemp) / (minAdaptTemp - lowerBoundTemp);
        }
    }

    /**
     * 当外界温度 很高的时候，玩家散热的效率应该下降
     *
     * @param envTemp
     * @return
     */
    public static double getPlayerLose(double envTemp, PlayerEntity player) {
        final double upperBoundTemp = 200; // 环境温度的上限
        double minAdaptTemp = NORMAL_TEM + BASIC_ADAPTATION_TEMP + getPlayerAdaptionTemp(player); // 最大适应温度

        if (envTemp > upperBoundTemp) {
            return 0; // 如果环境温度高于200，效率为0
        } else if (envTemp < minAdaptTemp) {
            return 1; // 如果环境温度低于最大适应温度，效率为1
        } else {
            // 在两个极端之间线性插值
            return 1 - (envTemp - minAdaptTemp) / (upperBoundTemp - minAdaptTemp);
        }
    }

}
