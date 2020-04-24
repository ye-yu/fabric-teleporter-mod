package fp.yeyu.teleportermod.utils;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class Particles {
    public static final Identifier AOT_PARTICLE_ID = new Identifier(TeleporterMod.NAMESPACE, ArrowOfTeleportationItem.MOD_NAME + "_particle");

    public static PacketConsumer playParticleOnPlayer() {
        return (context, data) -> {
            final PlayerEntity player = context.getPlayer();
            final int count = data.readInt();
            spawnParticlesOnEntity(null, player, count);
        };
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
