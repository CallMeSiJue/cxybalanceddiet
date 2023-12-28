package cxy.cxybalanceddiet.attribute;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.network.PacketByteBuf;

public class ThirstManager extends ClampedEntityAttribute {

    public static final String NBT_VALUE = "nbt_value";
    public static final String NBT_CHURN_VALUE = "nbt_churn_value";
    private static final String KEY = Cxybalanceddiet.MOD_ID + ".thirst";
    /**
     * 口渴值
     */
    private double value = 20;
    /**
     * 每次流失
     */
    private double churnValue = 1;

    public ThirstManager() {
        super(KEY, 20, 0, 20);
    }

    public double getChurnValue() {
        return churnValue;
    }

    public void setChurnValue(double churnValue) {
        this.churnValue = churnValue;
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
        this.value = 20;
        this.churnValue = 1;
    }

    public void writeToData(PacketByteBuf data) {
        data.writeDouble(this.value);
        data.writeDouble(this.churnValue);
    }

    public void readFromData(PacketByteBuf data) {
        this.value = data.readDouble();
        this.churnValue = data.readDouble();

    }
}
