package survivalblock.axolotlamour.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import survivalblock.axolotlamour.client.entity.AmourModelLayers;

public class AxolotlAmourClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(AmourModelLayers.AXOLOTL_ARMOR, AmourModelLayers::getTexturedModelData);
    }
}
