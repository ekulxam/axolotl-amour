package survivalblock.axolotlamour.access;

import net.minecraft.entity.damage.DamageSource;

public interface AxolotlArmorAccess {

    boolean axolotl_amour$hasArmor();
    boolean axolotl_amour$shouldArmorAbsorbDamage(DamageSource source);
}
