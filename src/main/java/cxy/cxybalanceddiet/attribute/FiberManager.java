package cxy.cxybalanceddiet.attribute;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.network.PacketByteBuf;

public class FiberManager extends ClampedEntityAttribute {

    public static final String NBT_VALUE = "nbt_fiber_value";

    private static final String KEY = Cxybalanceddiet.MOD_ID + ".fiber";
    /**
     * 口渴值
     */
    private double value = 70;


    public FiberManager() {
        super(KEY, 70, 0, 100);
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = clamp(value);
    }

    public void copyFrom(FiberManager f) {

        this.value = f.getValue();
    }

    public void add(double value) {
        this.value = clamp(this.value + value);
    }

    public void reset() {
        this.value = 20;
    }

    public void writeToData(PacketByteBuf data) {
        data.writeDouble(this.value);
    }

    public void readFromData(PacketByteBuf data) {
        this.value = data.readDouble();
    }

    public Integer regenerationSpeed() {
        if (value < 40) {
            return 20;
        } else if (value < 80) {
            return 10;
        } else {
            return 5;
        }
    }
}
