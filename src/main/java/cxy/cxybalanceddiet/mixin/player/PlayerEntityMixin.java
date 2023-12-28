package cxy.cxybalanceddiet.mixin.player;


import cxy.cxybalanceddiet.attribute.Accessor;
import cxy.cxybalanceddiet.attribute.ThirstManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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


    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(@NotNull NbtCompound nbt, CallbackInfo info) {
        if (this.getWorld().isClient) {
            return;
        }
        this.thirstManager.setValue(nbt.contains(ThirstManager.NBT_VALUE, NbtElement.DOUBLE_TYPE) ? nbt.getDouble(ThirstManager.NBT_VALUE) : 20);
        this.thirstManager.setChurnValue(nbt.contains(ThirstManager.NBT_CHURN_VALUE, NbtElement.FLOAT_TYPE) ? nbt.getDouble(ThirstManager.NBT_CHURN_VALUE) : 0.5F);

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(@NotNull NbtCompound nbt, CallbackInfo info) {
        if (this.getWorld().isClient) {
            return;
        }
        nbt.putDouble(ThirstManager.NBT_VALUE, this.thirstManager.getValue());
        nbt.putDouble(ThirstManager.NBT_CHURN_VALUE, this.thirstManager.getChurnValue());

    }

    @Inject(method = "addExhaustion", at = @At("TAIL"))
    public void addExhaustion(float exhaustion, CallbackInfo ci) {
        if (this.getWorld().isClient) {
            return;
        }
        this.thirstManager.add(-exhaustion / 10); //70 originally

    }

}
