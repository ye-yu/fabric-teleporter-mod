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
            /*
            * PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            * passedData.writeInt(30); // count
            * passedData.writeInt(13548494); // color
            * passedData.writeDouble(target.getParticleX(0.5D)); // position x
            * passedData.writeDouble(target.getParticleZ(0.5D)); // position y
            * */
            final int count = data.readInt();
            int color = data.readInt();
            final double particleX = data.readDouble();
            final double particleY = player.getRandomBodyY();
            final double particleZ = data.readDouble();
            spawnParticlesOnPosition(
                    color,
                    ParticleTypes.ENTITY_EFFECT,
                    particleX,
                    particleY,
                    particleZ,
                    count);
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
        int color = 13548494;
        for(int k = 0; k < count; ++k) {
            double x = blockPos.getX() + Math.random();
            double y = blockPos.getY();
            double z = blockPos.getZ() + Math.random();
            double d = (double)(color >> 16 & 255) / 255.0D;
            double e = (double)(color >> 8 & 255) / 255.0D;
            double f = (double)(color & 255) / 255.0D;
            MinecraftClient.getInstance().particleManager.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, d, e, f);
        }
    }

    public static void spawnParticlesOnPosition(Integer color, ParticleEffect effect, double x, double y, double z, int count) {
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
