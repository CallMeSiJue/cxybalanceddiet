package cxy.cxybalanceddiet.attribute;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import cxy.cxybalanceddiet.utils.TemHandler;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TempManager extends ClampedEntityAttribute {
    public static final String NBT_VALUE = "nbt_temp_value";
    public static final String NBT_COLD_TICK_VALUE = "nbt_temp_cold_tick_value";
    public static final String NBT_HOT_TICK_VALUE = "nbt_temp_hot_tick_value";
    private static final Logger log = LoggerFactory.getLogger(TempManager.class);
    private static final String KEY = Cxybalanceddiet.MOD_ID + ".temp";
    public int coldTick = 0;
    public int hotTick = 0;
    /**
     * 口渴值
     */
    private double value = 26;
    private double envTemp = 26;

    public TempManager() {
        super(KEY, 26, -200, 200);
    }

    public int getColdTick() {
        return coldTick;
    }

    public void setColdTick(int coldTick) {
        this.coldTick = coldTick;
    }

    public int getHotTick() {
        return hotTick;
    }

    public void setHotTick(int hotTick) {
        this.hotTick = hotTick;
    }

    public double getEnvTemp() {
        return envTemp;
    }

    public void setEnvTemp(double envTemp) {
        this.envTemp = envTemp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = clamp(value);
    }

    public void add(double value) {
        this.value = clamp(this.value + value);
    }

    public void reset() {
        this.value = 26;
        this.coldTick = 0;
        this.hotTick = 0;
    }

    public void writeToData(PacketByteBuf data) {
        data.writeDouble(this.value);
        data.writeDouble(this.envTemp);
    }

    public void writeToServer(PacketByteBuf data) {
        data.writeInt(this.coldTick);
        data.writeInt(this.hotTick);
    }

    public void readFromClient(PacketByteBuf data) {
        this.coldTick = data.readInt();
        this.hotTick = data.readInt();
    }

    public void readFromData(PacketByteBuf data) {
        this.value = data.readDouble();
        this.envTemp = data.readDouble();
    }

    public Integer getTempStatus(PlayerEntity player) {
        double playerAdaptionTemp = TemHandler.getPlayerAdaptionTemp(player);
        double normalTem = TemHandler.NORMAL_TEM;
        double basicAdaptationTemp = TemHandler.BASIC_ADAPTATION_TEMP;
        if (value < normalTem - 2 * (basicAdaptationTemp + playerAdaptionTemp)) {
            return -2;
        } else if (value < normalTem - (basicAdaptationTemp + playerAdaptionTemp)) {
            return -1;
        } else if (value < normalTem + (basicAdaptationTemp + playerAdaptionTemp)) {
            return 0;
        } else if (value < normalTem + 2 * (basicAdaptationTemp + playerAdaptionTemp)) {
            return 1;
        } else {
            return 2;
        }
    }

    public void copyFrom(TempManager fatManager) {
        this.coldTick = fatManager.coldTick;
        this.hotTick = fatManager.hotTick;

    }
}
