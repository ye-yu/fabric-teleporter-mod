package fp.yeyu.teleportermod.utils;

import fp.yeyu.teleportermod.TeleporterMod;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Commands {

    public static void randomTeleport(World world, Entity entity, int lower, int upper) {
        final Vec3d center = entity.getPos();
        final int range = upper - lower;
        double x = center.getX() + lower + (2 * Math.random() - 1) * range;
        double z = center.getZ() + lower + (2 * Math.random() - 1) * range;
        double y = getY(world, x, z);
        entity.requestTeleport(x, y, z);
        BlockPos tpPosition = new BlockPos(x, y, z);
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeBlockPos(tpPosition);
        if (world.isClient())
            ClientSidePacketRegistry.INSTANCE.sendToServer(TeleporterMod.REQUEST_TP_ID, data);
    }

    public static int getY(BlockView blockView, double destX, double destZ) {
        BlockPos blockPos = new BlockPos(destX, 256.0D, destZ);

        do {
            if (blockPos.getY() <= 0) {
                return 257;
            }

            blockPos = blockPos.down();
        } while(blockView.getBlockState(blockPos).isAir());

        return blockPos.getY() + 1;
    }

    public static void teleportPacket(PacketContext context, PacketByteBuf data) {
        BlockPos pos = data.readBlockPos();
        context.getTaskQueue().execute(() -> context.getPlayer().teleport(pos.getX(), pos.getY(), pos.getZ()));
    }
}
