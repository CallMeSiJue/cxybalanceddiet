package cxy.cxybalanceddiet.attribute;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.network.PacketByteBuf;

public class TempManager extends ClampedEntityAttribute {

    public static final String NBT_VALUE = "nbt_temp_value";

    private static final String KEY = Cxybalanceddiet.MOD_ID + ".temp";
    /**
     * 口渴值
     */
    private double value = 26;

    private double envTemp = 26;

    public TempManager() {
        super(KEY, 26, -200, 200);
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
    }

    public void writeToData(PacketByteBuf data) {
        data.writeDouble(this.value);
        data.writeDouble(this.envTemp);
    }

    public void readFromData(PacketByteBuf data) {
        this.value = data.readDouble();
        this.envTemp = data.readDouble();
    }

    public void copyFrom(TempManager fatManager) {
        this.value = fatManager.value;
    }
}
