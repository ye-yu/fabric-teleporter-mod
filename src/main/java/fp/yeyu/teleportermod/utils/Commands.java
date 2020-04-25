package fp.yeyu.teleportermod.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Commands {
    public static void rtp(World world, Entity entity, int lower, int upper) {
        final Vec3d center = entity.getPos();
        final int range = upper - lower;
        double x = center.getX() + lower + (2 * Math.random() - 1) * range;
        double z = center.getZ() + lower + (2 * Math.random() - 1) * range;
        double y = getY(world, x, z);
        System.out.println("Teleportation distance: " + center.distanceTo(new Vec3d(x, y, z)));
        entity.requestTeleport(x, y, z);
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
}
