package fp.yeyu.teleportermod.items.teleporterarrow;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.utils.Commands;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class ArrowOfTeleportationItemEntity extends ArrowEntity {

    private static final int[] range = new int[]{5, 10};
    private int tpMultiplier = 1;
    private static String PUNCH_TP_MULTIPLIER_ID = "punch_tp_multiplier";

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
    protected void onHit(LivingEntity target) {
        final int min = range[0] + 5*(this.tpMultiplier - 1);
        final int max = range[1] + 5*(this.tpMultiplier - 1);
        Commands.rtp(world, target, min, max);
        playParticle(target.getEntityWorld(), new ChunkPos(target.getBlockPos()), target);
    }

    private void playParticle(World world, ChunkPos pos, Entity target) {
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,pos);

        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeInt(30); // count
        passedData.writeInt(13548494); // color
        passedData.writeDouble(target.getParticleX(0.5D)); // position x
        passedData.writeDouble(target.getParticleZ(0.5D)); // position y

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

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(PUNCH_TP_MULTIPLIER_ID, this.tpMultiplier);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.setTpMultiplier(tag.getInt(PUNCH_TP_MULTIPLIER_ID));
    }

    public void setTpMultiplier(int lvl) {
        this.tpMultiplier = lvl;
    }
}