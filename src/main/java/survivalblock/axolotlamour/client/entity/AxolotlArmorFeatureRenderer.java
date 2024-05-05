package survivalblock.axolotlamour.client.entity;

import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.Cracks;
import net.minecraft.entity.passive.Cracks.CrackLevel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;
import survivalblock.axolotlamour.access.AxolotlArmorAccess;
import survivalblock.axolotlamour.common.item.AxolotlArmorItem;

import static survivalblock.axolotlamour.common.AxolotlAmour.id;

@Environment(EnvType.CLIENT)
public class AxolotlArmorFeatureRenderer extends FeatureRenderer<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> {
    private final AxolotlEntityModel<AxolotlEntity> model;
    private static final Map<Cracks.CrackLevel, Identifier> CRACK_TEXTURES = Map.of(CrackLevel.LOW, id("textures/entity/axolotl/axolotl_armor_crackiness_low.png"), CrackLevel.MEDIUM, id("textures/entity/axolotl/axolotl_armor_crackiness_medium.png"), CrackLevel.HIGH, id("textures/entity/axolotl/axolotl_armor_crackiness_high.png"));

    public AxolotlArmorFeatureRenderer(FeatureRendererContext<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> context, EntityModelLoader loader) {
        super(context);
        this.model = new AxolotlEntityModel<>(loader.getModelPart(AmourModelLayers.AXOLOTL_ARMOR));
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AxolotlEntity axolotl, float f, float g, float h, float j, float k, float l) {
        if (((AxolotlArmorAccess) axolotl).axolotl_amour$hasArmor()) {
            ItemStack itemStack = axolotl.getBodyArmor();
            Item var13 = itemStack.getItem();
            if (var13 instanceof AxolotlArmorItem animalArmorItem) {
                this.getContextModel().copyStateTo(this.model);
                this.model.animateModel(axolotl, f, g, h);
                this.model.setAngles(axolotl, f, g, j, k, l);
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(animalArmorItem.getEntityTexture()));
                this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                this.renderDyed(matrixStack, vertexConsumerProvider, i, itemStack, animalArmorItem);
                this.renderCracks(matrixStack, vertexConsumerProvider, i, itemStack);
            }

        }
    }

    private void renderDyed(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, AxolotlArmorItem item) {
        if (stack.isIn(ItemTags.DYEABLE)) {
            int i = DyedColorComponent.getColor(stack, 0);
            if (Argb.getAlpha(i) == 0) {
                return;
            }

            Identifier identifier = item.getOverlayTexture();
            if (identifier == null) {
                return;
            }

            float f = (float)Argb.getRed(i) / 255.0F;
            float g = (float)Argb.getGreen(i) / 255.0F;
            float h = (float)Argb.getBlue(i) / 255.0F;
            this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(identifier)), light, OverlayTexture.DEFAULT_UV, f, g, h, 1.0F);
        }

    }

    private void renderCracks(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack) {
        Cracks.CrackLevel crackLevel = Cracks.WOLF_ARMOR.getCrackLevel(stack);
        if (crackLevel != CrackLevel.NONE) {
            Identifier identifier = CRACK_TEXTURES.get(crackLevel);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(identifier));
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
