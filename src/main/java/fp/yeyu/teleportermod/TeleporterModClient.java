package fp.yeyu.teleportermod;

import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItemEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class TeleporterModClient implements ClientModInitializer {
    public static final String NAMESPACE = TeleporterMod.NAMESPACE;
    public static final EntityType<ArrowOfTeleportationItemEntity> ARROW_OF_TELEPORTATION_ITEM_ENTITY = TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY;

    public void onInitializeClient() {
        // registering arrow of teleportation client side rendering
        EntityRendererRegistry.INSTANCE.register(ARROW_OF_TELEPORTATION_ITEM_ENTITY, (entityRenderDispatcher, context) -> new ArrowEntityRenderer(entityRenderDispatcher));
    }
}
