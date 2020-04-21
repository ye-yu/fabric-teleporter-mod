package fp.yeyu.teleportermod;

import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItemEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public class TeleporterModClient implements ClientModInitializer {
    public static final String NAMESPACE = TeleporterMod.NAMESPACE;
    public static final EntityType<ArrowOfTeleportationItemEntity> ARROW_OF_TELEPORTATION_ITEM_ENTITY = TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY;

    public void onInitializeClient() {
        // registering arrow of teleportation client side rendering
        EntityRendererRegistry.INSTANCE.register(ARROW_OF_TELEPORTATION_ITEM_ENTITY, (entityRenderDispatcher, context) -> new ArrowEntityRenderer(entityRenderDispatcher));
        ClientSidePacketRegistry.INSTANCE.register(new Identifier(NAMESPACE, "arrow_of_teleportation"), TeleporterModClient::spawnEntity);
    }

    private static void spawnEntity(PacketContext context, PacketByteBuf buffer) {
        int entityId = buffer.readInt();
        UUID uuid = buffer.readUuid();
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        context.getTaskQueue().execute(() -> {
            ArrowOfTeleportationItemEntity entity = new ArrowOfTeleportationItemEntity(context.getPlayer().world, context.getPlayer());
            entity.setPos(x, y, z);
            entity.setEntityId(entityId);
            entity.setUuid(uuid);
            entity.updateTrackedPosition(x, y, z);
            ((ClientWorld) context.getPlayer().world).addEntity(entityId, entity);
        });
    }
}
