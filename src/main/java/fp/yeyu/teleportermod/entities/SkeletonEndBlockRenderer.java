package fp.yeyu.teleportermod.entities;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorBipedFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.util.Identifier;

public class SkeletonEndBlockRenderer extends MobEntityRenderer<SkeletonEndBlockEntity, SkeletonEntityModel<SkeletonEndBlockEntity>> {

    public SkeletonEndBlockRenderer(EntityRenderDispatcher renderManager, SkeletonEntityModel<SkeletonEndBlockEntity> model, float f) {
        super(renderManager, model, f);
    }

    public SkeletonEndBlockRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SkeletonEntityModel<>(), 1);
        this.addFeature(new HeldItemFeatureRenderer<>(this));
        this.addFeature(new ArmorBipedFeatureRenderer<>(this, new SkeletonEntityModel<>(0.5F, true), new SkeletonEntityModel<>(1.0F, true)));
    }

    @Override
    public Identifier getTexture(SkeletonEndBlockEntity entity) {
        return new Identifier("textures/entity/skeleton/skeleton.png");
    }
}
