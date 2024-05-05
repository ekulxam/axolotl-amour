/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package survivalblock.axolotlamour.common.item;

import java.util.function.Function;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.axolotlamour.access.AxolotlArmorAccess;
import survivalblock.axolotlamour.common.AxolotlAmour;
import survivalblock.axolotlamour.common.init.AxolotlAmourItems;

public class AxolotlArmorItem
        extends ArmorItem {
    private final Identifier entityTexture;
    @Nullable
    private final Identifier overlayTexture;

    public AxolotlArmorItem(RegistryEntry<ArmorMaterial> material, boolean hasOverlay, Item.Settings settings) {
        super(material, ArmorItem.Type.BODY, settings);
        Identifier identifier = AxolotlAmour.id("textures/entity/axolotl/axolotl_armor");
        this.entityTexture = identifier.withSuffixedPath(".png");
        this.overlayTexture = hasOverlay ? identifier.withSuffixedPath("_overlay.png") : null;
    }

    public Identifier getEntityTexture() {
        return this.entityTexture;
    }

    @Nullable
    public Identifier getOverlayTexture() {
        return this.overlayTexture;
    }

    @Override
    public SoundEvent getBreakSound() {
        return SoundEvents.ITEM_SHIELD_BREAK;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        World world = user.getWorld();
        if (entity instanceof AxolotlEntity axolotl) {
            if (!world.isClient() && stack.isOf(AxolotlAmourItems.AXOLOTL_ARMOR) && !((AxolotlArmorAccess) axolotl).axolotl_amour$hasArmor() && !axolotl.isBaby()) {
                axolotl.equipBodyArmor(stack.copyWithCount(1));
                stack.decrementUnlessCreative(1, user);
            }
            return ActionResult.success(world.isClient());
        }
        return super.useOnEntity(stack, user, entity, hand);
    }
}

