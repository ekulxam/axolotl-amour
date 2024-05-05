package survivalblock.axolotlamour.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import survivalblock.axolotlamour.access.AxolotlArmorAccess;

@Mixin(AxolotlEntity.class)
public abstract class AxolotlEntityMixin extends AnimalEntity implements AxolotlArmorAccess {

    protected AxolotlEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean axolotl_amour$hasArmor() {
        return !this.getBodyArmor().isEmpty();
    }

    @Override
    public boolean axolotl_amour$shouldArmorAbsorbDamage(DamageSource source) {
        return this.axolotl_amour$hasArmor() && !source.isIn(DamageTypeTags.BYPASSES_WOLF_ARMOR);
    }
}
