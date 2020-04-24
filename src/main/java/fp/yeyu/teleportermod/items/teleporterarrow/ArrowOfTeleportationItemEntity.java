package fp.yeyu.teleportermod.items.teleporterarrow;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.blocks.TeleporterPlate;
import fp.yeyu.teleportermod.utils.Commands;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Stream;

public class ArrowOfTeleportationItemEntity extends ArrowEntity {

    public ArrowOfTeleportationItemEntity(EntityType<? extends ArrowOfTeleportationItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowOfTeleportationItemEntity(World world, LivingEntity owner) {
        this(world, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ()); // obtained from ProjectileEntity.java
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = ProjectileEntity.PickupPermission.ALLOWED;
        }
    }

    public ArrowOfTeleportationItemEntity(World world, double x, double y, double z){
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, world);
        this.setPos(x, y, z);
    }

    public ArrowOfTeleportationItemEntity(World world) {
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        LOGGER.info("Arrow hit on block " + hitResult.getType());
    }

    @Override
    protected void onHit(LivingEntity target) {
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, 20, 0);
        target.addStatusEffect(statusEffectInstance);
        LOGGER.info("Arrow hit target " + target.getEntityName());
        final World entityWorld = target.getEntityWorld();
        final List<Integer> integers = TeleporterPlate.teleportationStrengthLevel.get(TeleporterPlate.TeleportationStrengthLevel.BASIC);
        final int min = integers.get(0);
        final int max = integers.get(1);
        Commands.setCommandFeedbackOutput(target, false);
        if (!Commands.spreadPlayerSelf(world, target, min, max)) {
            LOGGER.info(String.format("Entity %s cannot use teleportation.%n", target.getName()));
        }
        playParticle(target.getEntityWorld(), new ChunkPos(target.getBlockPos()));
    }

    private void playParticle(World world, ChunkPos pos) {
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,pos);

        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeInt(30);

        watchingPlayers.forEach(player ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, TeleporterMod.AOT_PARTICLE_ID, passedData));
    }

    protected ItemStack asItemStack() {
        return new ItemStack(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM);
    }

    public void initFromStack(ItemStack stack) { }

    @Override
    public Packet<?> createSpawnPacket() {
        super.createSpawnPacket();
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(this.getEntityId());
        buf.writeUuid(this.getUuid());
        buf.writeDouble(this.getX());
        buf.writeDouble(this.getY());
        buf.writeDouble(this.getZ());
        return new CustomPayloadS2CPacket(new Identifier(TeleporterMod.NAMESPACE, "arrow_of_teleportation"), buf);
    }
}