package cxy.cxybalanceddiet.attribute;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.network.PacketByteBuf;

public class ProteinManager extends ClampedEntityAttribute {

    public static final String NBT_VALUE = "nbt_protein_value";

    private static final String KEY = Cxybalanceddiet.MOD_ID + ".protein";
    /**
     * 口渴值
     */
    private double value = 70;


    public ProteinManager() {
        super(KEY, 70, 0, 100);
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
    }

    public void writeToData(PacketByteBuf data) {
        data.writeDouble(this.value);
    }

    public void readFromData(PacketByteBuf data) {
        this.value = data.readDouble();
    }

    public void copyFrom(ProteinManager f) {

        this.value = f.getValue();
    }
}
