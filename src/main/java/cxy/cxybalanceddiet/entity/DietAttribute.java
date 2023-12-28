package cxy.cxybalanceddiet.entity;

import cxy.cxybalanceddiet.Cxybalanceddiet;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;

public class DietAttribute {
    public static final EntityAttribute FAT = new ClampedEntityAttribute(Cxybalanceddiet.MOD_ID+".fat", 0.0D, 0.0D, 100.0D).setTracked(true);
    public static final EntityAttribute PROTEIN = new ClampedEntityAttribute(Cxybalanceddiet.MOD_ID+".protein", 0.0D, 0.0D, 100.0D).setTracked(true);
    public static final EntityAttribute FIBER = new ClampedEntityAttribute(Cxybalanceddiet.MOD_ID+".fiber", 0.0D, 0.0D, 100.0D).setTracked(true);

    public static void register() {

    }
}
