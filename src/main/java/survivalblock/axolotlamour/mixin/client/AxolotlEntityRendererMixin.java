package survivalblock.axolotlamour.mixin.client;

import net.minecraft.client.render.entity.AxolotlEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.WolfArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.AxolotlEntityModel;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.axolotlamour.client.entity.AxolotlArmorFeatureRenderer;

@Mixin(AxolotlEntityRenderer.class)
public abstract class AxolotlEntityRendererMixin extends MobEntityRenderer<AxolotlEntity, AxolotlEntityModel<AxolotlEntity>> {
	public AxolotlEntityRendererMixin(EntityRendererFactory.Context context, AxolotlEntityModel<AxolotlEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(at = @At("RETURN"), method = "<init>")
	private void addAxolotlArmor(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new AxolotlArmorFeatureRenderer(this, context.getModelLoader()));
	}
}