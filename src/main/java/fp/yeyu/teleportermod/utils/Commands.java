package fp.yeyu.teleportermod.utils;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import java.util.Objects;

public class Commands {
    public static boolean spreadPlayerSelf(World world, Entity entity, int lower, int upper) {
        final MinecraftServer server = world.getServer();
        if (Objects.isNull(server)) return false;
        final ServerCommandSource commandSource = entity.getCommandSource();
        server.getCommandManager().execute(commandSource, getSpreadCommand(lower, upper));
        return true;
    }

    private static String getSpreadCommand(int min, int max) {
        return String.format("/spreadplayers ~ ~ %d %d false @s", min, max);
    }
}
