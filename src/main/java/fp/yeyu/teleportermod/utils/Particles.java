package fp.yeyu.teleportermod.utils;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.blocks.TeleporterPlate;
import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class Particles {
    public static final Identifier AOT_PARTICLE_ID = constructIdentifier(ArrowOfTeleportationItem.MOD_NAME);
    public static final Identifier TPLATE_PARTICLE_ID = constructIdentifier(TeleporterPlate.MOD_NAME);

    public static Identifier constructIdentifier(String name) {
        return new Identifier(TeleporterMod.NAMESPACE, name + "_particle");
    }

    public static PacketConsumer playParticleOnPlayer() {
        return (context, data) -> {
            final PlayerEntity player = context.getPlayer();
            final int count = data.readInt();
            int color = data.readInt();
            spawnParticlesOnEntity(color, player, count);
        };
    }


    public static PacketConsumer playTeleporterPlateParticle() {
        return (context, data) -> {
            final BlockPos blockPos = data.readBlockPos();
            final int count = data.readInt();
            spawnParticleOnBlockPos(blockPos, count);
        };
    }

    private static void spawnParticleOnBlockPos(BlockPos blockPos, int count) {
        for(int k = 0; k < count; ++k) {
            double x = blockPos.getX() + Math.random();
            double y = blockPos.getY();
            double z = blockPos.getY() + Math.random();
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.5, 0);
        }
    }


    public static void spawnParticlesOnEntity(Integer color, PlayerEntity player, int count) {
        spawnParticlesOnPosition(
                color,
                ParticleTypes.ENTITY_EFFECT,
                player.world,
                player.getParticleX(0.5D),
                player.getRandomBodyY(),
                player.getParticleZ(0.5D),
                count
                );
    }

    public static void spawnParticlesOnPosition(Integer color, ParticleEffect effect, World world, double x, double y, double z, int count) {
        if (Objects.isNull(color)) {
            color = 29;
        }
        if (count > 0) {
            double d = (double)(color >> 16 & 255) / 255.0D;
            double e = (double)(color >> 8 & 255) / 255.0D;
            double f = (double)(color & 255) / 255.0D;

            for(int k = 0; k < count; ++k) {
                MinecraftClient.getInstance().particleManager.addParticle(effect, x, y, z, d, e, f);
            }
        }
    }
}
