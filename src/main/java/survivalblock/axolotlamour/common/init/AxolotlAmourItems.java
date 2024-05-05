package survivalblock.axolotlamour.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.axolotlamour.common.AxolotlAmour;
import survivalblock.axolotlamour.common.item.AxolotlArmorItem;

public class AxolotlAmourItems {

    public static final Item AXOLOTL_ARMOR = registerItem("axolotl_armor", new AxolotlArmorItem(ArmorMaterials.TURTLE, false, new Item.Settings().maxDamage(ArmorItem.Type.BODY.getMaxDamage(4))));

    @SuppressWarnings("SameParameterValue")
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, AxolotlAmour.id(name), item);
    }

    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(Items.WOLF_ARMOR, AXOLOTL_ARMOR));
    }
}
