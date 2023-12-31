package cxy.cxybalanceddiet.mixin.player;


import cxy.cxybalanceddiet.attribute.*;
import cxy.cxybalanceddiet.config.FoodConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
@SuppressWarnings({"CanBeFinal", "AddedMixinMembersNamePattern"})
public abstract class PlayerEntityMixin extends LivingEntity implements Accessor {

    @Shadow
    public int experienceLevel;
    @Shadow
    protected HungerManager hungerManager;
    @Shadow
    protected boolean isSubmergedInWater;
    @Unique
    protected ThirstManager thirstManager = new ThirstManager();
    @Unique
    protected FatManager fatManager = new FatManager();
    @Unique
    protected FiberManager fiberManager = new FiberManager();
    @Unique
    protected ProteinManager proteinManager = new ProteinManager();

    @Unique
    protected TempManager tempManager = new TempManager();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract PlayerInventory getInventory();

    @Shadow
    public abstract HungerManager getHungerManager();

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    @Shadow
    public abstract Text getName();


    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract boolean isSpectator();

    @Shadow
    public abstract void remove(RemovalReason reason);

    @Shadow
    public abstract void increaseStat(Identifier stat, int amount);

    @Shadow
    public abstract void addExhaustion(float exhaustion);

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract float getMovementSpeed();

    @Shadow
    public abstract boolean isSwimming();

    @Shadow
    public abstract void setFireTicks(int fireTicks);

    @Unique
    @Override
    public ThirstManager getThirstManager() {
        return this.thirstManager;
    }

    @Unique
    @Override
    public FatManager getFatManager() {
        return this.fatManager;
    }

    @Unique
    @Override
    public FiberManager getFiberManager() {
        return this.fiberManager;
    }

    @Unique
    @Override
    public ProteinManager getProteinManager() {
        return this.proteinManager;
    }

    @Unique
    @Override
    public TempManager getTempManager() {
        return this.tempManager;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(@NotNull NbtCompound nbt, CallbackInfo info) {
        if (this.getWorld().isClient) {
            return;
        }
        this.thirstManager.setValue(nbt.contains(ThirstManager.NBT_VALUE, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(ThirstManager.NBT_VALUE) : 70);
        this.thirstManager.setChurnValue(nbt.contains(ThirstManager.NBT_CHURN_VALUE, NbtElement.FLOAT_TYPE) ? nbt.getDouble(ThirstManager.NBT_CHURN_VALUE) : 0.5F);

        //fat
        this.fatManager.setValue(nbt.contains(FatManager.NBT_VALUE, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(FatManager.NBT_VALUE) : 70);
        // fiber
        this.fiberManager.setValue(nbt.contains(FiberManager.NBT_VALUE, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(FiberManager.NBT_VALUE) : 70);
        // protein
        this.proteinManager.setValue(nbt.contains(ProteinManager.NBT_VALUE, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(ProteinManager.NBT_VALUE) : 70);
        // temp
        this.tempManager.setValue(nbt.contains(TempManager.NBT_VALUE, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(TempManager.NBT_VALUE) : 26.5);
        this.tempManager.setColdTick(nbt.contains(TempManager.NBT_COLD_TICK_VALUE, NbtElement.INT_TYPE) ? nbt.getInt(TempManager.NBT_COLD_TICK_VALUE) : 0);
        this.tempManager.setHotTick(nbt.contains(TempManager.NBT_HOT_TICK_VALUE, NbtElement.INT_TYPE) ? nbt.getInt(TempManager.NBT_HOT_TICK_VALUE) : 0);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(@NotNull NbtCompound nbt, CallbackInfo info) {
        if (this.getWorld().isClient) {
            return;
        }
        nbt.putDouble(ThirstManager.NBT_VALUE, this.thirstManager.getValue());
        nbt.putDouble(ThirstManager.NBT_CHURN_VALUE, this.thirstManager.getChurnValue());
        //fat
        nbt.putDouble(FatManager.NBT_VALUE, this.fatManager.getValue());
        // fiber
        nbt.putDouble(FiberManager.NBT_VALUE, this.fiberManager.getValue());
        // protein
        nbt.putDouble(ProteinManager.NBT_VALUE, this.proteinManager.getValue());
        // temp
        nbt.putDouble(TempManager.NBT_VALUE, this.tempManager.getValue());
        nbt.putInt(TempManager.NBT_COLD_TICK_VALUE, this.tempManager.getColdTick());
        nbt.putInt(TempManager.NBT_HOT_TICK_VALUE, this.tempManager.getHotTick());
    }

    @Inject(method = "eatFood", at = @At("HEAD"))
    public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        Item item = stack.getItem();
        String name = item.getTranslationKey();
        FoodComponent food = item.getFoodComponent();
        if (food == null) {
            return;
        }
        FoodConfig foodConfig = FoodConfig.getInstance();
        Double fatValue = foodConfig.containsInKey(name, foodConfig.fatFood);
        this.fatManager.add(fatValue);
        Double fiberValue = foodConfig.containsInKey(name, foodConfig.fiberFood);
        this.fiberManager.add(fiberValue);
        Double proteinValue = foodConfig.containsInKey(name, foodConfig.proteinFood);
        this.proteinManager.add(proteinValue);
        Double thirstValue = foodConfig.containsInKey(name, foodConfig.waterFood);
        this.thirstManager.add(thirstValue);
    }

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    public void addExhaustion(float exhaustion, CallbackInfo ci) {
        if (this.getWorld().isClient) {
            return;
        }
        this.thirstManager.add(-exhaustion / 5); //70 originally
        this.fatManager.add(-exhaustion / 20);
        this.fiberManager.add(-exhaustion / 30);
        this.proteinManager.add(-exhaustion / 15);

    }

}
