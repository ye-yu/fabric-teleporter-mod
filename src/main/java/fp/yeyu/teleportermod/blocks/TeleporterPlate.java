package fp.yeyu.teleportermod.blocks;

import com.google.common.collect.Lists;
import fp.yeyu.teleportermod.utils.Commands;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TeleporterPlate extends PressurePlateBlock{
    public enum TeleportationStrengthLevel {
        BASIC, WOOD, LOG, IRON, GOLD, DIAMOND, EMERALD
    }
    private final ActivationRule type;
    public static final String MOD_NAME = "teleporter_plate";

    private static final Block[] validBlocks = new Block[]{
            // tp strength of 10 blocks
            Blocks.ACACIA_WOOD,
            Blocks.BIRCH_WOOD,
            Blocks.DARK_OAK_WOOD,
            Blocks.JUNGLE_WOOD,
            Blocks.OAK_WOOD,
            Blocks.SPRUCE_WOOD,
            Blocks.STRIPPED_ACACIA_WOOD,
            Blocks.STRIPPED_BIRCH_WOOD,
            Blocks.STRIPPED_DARK_OAK_WOOD,
            Blocks.STRIPPED_JUNGLE_WOOD,
            Blocks.STRIPPED_OAK_WOOD,
            Blocks.STRIPPED_SPRUCE_WOOD,

            // tp strength of 50 blocks
            Blocks.ACACIA_LOG,
            Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG,
            Blocks.JUNGLE_LOG,
            Blocks.OAK_LOG,
            Blocks.SPRUCE_LOG,
            Blocks.STRIPPED_ACACIA_LOG,
            Blocks.STRIPPED_BIRCH_LOG,
            Blocks.STRIPPED_DARK_OAK_LOG,
            Blocks.STRIPPED_JUNGLE_LOG,
            Blocks.STRIPPED_OAK_LOG,
            Blocks.STRIPPED_SPRUCE_LOG,

            // tp strength of 500 blocks
            Blocks.IRON_BLOCK,

            // tp strength of 2000 blocks
            Blocks.GOLD_BLOCK,

            // tp strength of 5000 blocks
            Blocks.DIAMOND_BLOCK,

            // tp strength of 12500 blocks
            Blocks.EMERALD_BLOCK
    };
    public static final HashMap<Block, List<Integer>> blockTeleportationStrength = new HashMap<>();
    public static final HashMap<TeleportationStrengthLevel, List<Integer>> teleportationStrengthLevel = new HashMap<>();

    static {
        teleportationStrengthLevel.put(TeleportationStrengthLevel.BASIC, Lists.newArrayList(2, 5));
        teleportationStrengthLevel.put(TeleportationStrengthLevel.WOOD, Lists.newArrayList(10, 50));
        teleportationStrengthLevel.put(TeleportationStrengthLevel.LOG, Lists.newArrayList(50, 100));
        teleportationStrengthLevel.put(TeleportationStrengthLevel.IRON, Lists.newArrayList(100, 500));
        teleportationStrengthLevel.put(TeleportationStrengthLevel.GOLD, Lists.newArrayList(500, 2000));
        teleportationStrengthLevel.put(TeleportationStrengthLevel.DIAMOND, Lists.newArrayList(2000, 5000));
        teleportationStrengthLevel.put(TeleportationStrengthLevel.EMERALD, Lists.newArrayList(5000, 12500));

        int woods = 12;
        for(int i=0; i < woods; i++) {
            blockTeleportationStrength.put(validBlocks[i], teleportationStrengthLevel.get(TeleportationStrengthLevel.WOOD));
        }

        for(int i=12; i < 2 * woods; i++) {
            blockTeleportationStrength.put(validBlocks[i], teleportationStrengthLevel.get(TeleportationStrengthLevel.LOG));
        }

        blockTeleportationStrength.put(Blocks.IRON_BLOCK, teleportationStrengthLevel.get(TeleportationStrengthLevel.IRON));
        blockTeleportationStrength.put(Blocks.GOLD_BLOCK, teleportationStrengthLevel.get(TeleportationStrengthLevel.GOLD));
        blockTeleportationStrength.put(Blocks.DIAMOND_BLOCK, teleportationStrengthLevel.get(TeleportationStrengthLevel.DIAMOND));
        blockTeleportationStrength.put(Blocks.EMERALD_BLOCK, teleportationStrengthLevel.get(TeleportationStrengthLevel.EMERALD));
    }

    private static boolean isValidBlock(Block block) {
        for(Block b: validBlocks) {
            if (b == block) return true;
        }
        return false;
    }

    public TeleporterPlate(PressurePlateBlock.ActivationRule type, Settings settings) {
        super(type, settings);
        this.type = type; // dont remove, used in getEntities method
    }

    @SuppressWarnings("rawtypes")
    private List getEntities(World world, BlockPos pos) {
        Box box = BOX.offset(pos);
        switch(this.type) {
            case EVERYTHING:
                return world.getEntities(null, box);
            case MOBS:
                return world.getNonSpectatingEntities(LivingEntity.class, box);
        }
        return null;
    }

    @Override
    protected void updateNeighbors(World world, BlockPos pos) {
        super.updateNeighbors(world, pos);
        BlockState blockState = world.getBlockState(pos);
        if(blockState.get(PressurePlateBlock.POWERED)) {
            this.onPressureStateOn(world, pos);
        }
    }

    @SuppressWarnings("rawtypes")
    private void onPressureStateOn(World world, BlockPos pos) {
        Block blockBelow = world.getBlockState(pos.add(0, -1, 0)).getBlock();
        List tpStrengthRange = blockTeleportationStrength.getOrDefault(blockBelow, teleportationStrengthLevel.get(TeleportationStrengthLevel.BASIC));
        List entities = this.getEntities(world, pos);
        if (Objects.nonNull(entities)) {
            for (Object o : entities) {
                Entity e = (Entity) o;
                Commands.setCommandFeedbackOutput(e, false);
                if (Commands.spreadPlayerSelf(world, e, (int) tpStrengthRange.get(0), (int) tpStrengthRange.get(1))) {
                    LOGGER.info("Spread player @s is successful.");
                } else {
                    LOGGER.info(String.format("Entity %s cannot use teleportation.%n", e.getName()));
                }
            }
        }
    }
}
