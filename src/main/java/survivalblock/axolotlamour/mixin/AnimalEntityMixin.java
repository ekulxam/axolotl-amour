package survivalblock.axolotlamour.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.axolotlamour.access.AxolotlArmorAccess;
import survivalblock.axolotlamour.common.AxolotlAmour;
import survivalblock.axolotlamour.common.init.AxolotlAmourItems;

@SuppressWarnings("UnreachableCode")
@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    protected AnimalEntityMixin(EntityType<? extends PassiveEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private void applyAxolotlDamage(DamageSource source, float amount, CallbackInfo ci){
        if (!((AnimalEntity) (Object) this instanceof AxolotlEntity axolotl)) {
            return;
        }
        if (!((AxolotlArmorAccess) axolotl).axolotl_amour$shouldArmorAbsorbDamage(source)) {
            return;
        }
        ItemStack itemStack = this.getBodyArmor();
        int i = itemStack.getDamage();
        int j = itemStack.getMaxDamage();
        itemStack.damage(MathHelper.ceil(amount), this, EquipmentSlot.BODY);
        if (Cracks.WOLF_ARMOR.getCrackLevel(i, j) != Cracks.WOLF_ARMOR.getCrackLevel(this.getBodyArmor())) {
            this.playSoundIfNotSilent(SoundEvents.ITEM_ARMOR_EQUIP_TURTLE.value());
            World world = this.getWorld();
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.TURTLE_SCUTE.getDefaultStack()), this.getX(), this.getY() + 1.0, this.getZ(), 20, 0.2, 0.1, 0.2, 0.1);
            }
        }
        ci.cancel();
    }

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;isBreedingItem(Lnet/minecraft/item/ItemStack;)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void equipAxolotlArmor(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, @Local ItemStack stack){
        if (!((AnimalEntity) (Object) this instanceof AxolotlEntity axolotl)) {
            return;
        }
        if (AxolotlAmour.hasTameableAxolotlsLoaded && !this.axolotl_amour$isTamed()) return;
        if (AxolotlAmour.hasTameableAxolotlsLoaded && !this.axolotl_amour$isOwner(player)) return;
        World world = axolotl.getWorld();
        if (stack.isOf(AxolotlAmourItems.AXOLOTL_ARMOR) && !((AxolotlArmorAccess) axolotl).axolotl_amour$hasArmor() && !axolotl.isBaby()) {
            if (!world.isClient()) {
                axolotl.equipBodyArmor(stack.copyWithCount(1));
                stack.decrementUnlessCreative(1, player);
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        }
        if (stack.isOf(Items.SHEARS) && ((AxolotlArmorAccess) axolotl).axolotl_amour$hasArmor() && (!EnchantmentHelper.hasBindingCurse(this.getBodyArmor()) || player.isCreative())) {
            if (!world.isClient()) {
                stack.damage(1, player, AxolotlEntity.getSlotForHand(hand));
                this.playSoundIfNotSilent(SoundEvents.ITEM_ARMOR_EQUIP_TURTLE.value());
                ItemStack itemStack2 = this.getBodyArmor();
                this.equipBodyArmor(ItemStack.EMPTY);
                this.dropStack(itemStack2);
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        }
        if (ArmorMaterials.ARMADILLO.value().repairIngredient().get().test(stack) && ((AxolotlArmorAccess) axolotl).axolotl_amour$hasArmor() && this.getBodyArmor().isDamaged()) {
            if (!world.isClient()) {
                stack.decrement(1);
                this.playSoundIfNotSilent(SoundEvents.ITEM_ARMOR_EQUIP_TURTLE.value());
                ItemStack itemStack2 = this.getBodyArmor();
                int i = (int)((float)itemStack2.getMaxDamage() * 0.125f);
                itemStack2.setDamage(Math.max(0, itemStack2.getDamage() - i));
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        }
    }

    @Unique
    private boolean axolotl_amour$isTamed(){
        return ((AnimalEntity) (Object) this) instanceof TameableEntity tameableEntity && tameableEntity.isTamed();
    }

    @Unique
    private boolean axolotl_amour$isOwner(LivingEntity entity){
        return ((AnimalEntity) (Object) this) instanceof TameableEntity tameableEntity && tameableEntity.isOwner(entity);
    }
}
