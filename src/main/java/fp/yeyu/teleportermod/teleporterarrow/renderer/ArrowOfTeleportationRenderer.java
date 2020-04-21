package fp.yeyu.teleportermod.teleporterarrow.renderer;

import fp.yeyu.teleportermod.TeleporterModClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;

public class ArrowOfTeleportationRenderer extends ProjectileEntityRenderer<ArrowEntity> {
    public ArrowOfTeleportationRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public Identifier getTexture(ArrowEntity entity) {
        return new Identifier(TeleporterModClient.NAMESPACE, "textures/entity/arrow.png");
    }
}
