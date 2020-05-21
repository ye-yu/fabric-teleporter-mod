package fp.yeyu.teleportermod.items;

import fp.yeyu.teleportermod.utils.Teleportations;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class TeleporterWand extends Item {
    public static final String MOD_NAME = "teleporter_wand";
    private enum Mode {
        ASCEND, NORMAL, DESCEND
    }

    public TeleporterWand(Settings settings) {
        super(settings);
    }

    private static void giveEntityEffect(PlayerEntity pe, int ticks) {
        pe.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, ticks));
        pe.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, ticks));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand)
    {
        if (playerEntity.hasVehicle()) {
            playerEntity.stopRiding();
        } else if (!playerEntity.onGround) {
            // do nothing
            return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
        }

        Mode mode = getTeleportationMode(playerEntity.getStackInHand(hand));
        if (mode == Mode.DESCEND) {
            return descendEntity(world, playerEntity, hand);
        }
        return ascendEntity(world, playerEntity, hand, mode == Mode.ASCEND);
    }

    private Mode getTeleportationMode(ItemStack stackInHand) {
        if (stackInHand.hasEnchantments()) {
            ListTag enchantments = stackInHand.getEnchantments();
            for(int i=0;i <enchantments.size();i++) {
                final CompoundTag compound = enchantments.getCompound(i);
                final String id = compound.getString("id");
                if(id.equals("teleportermod:ascend")) {
                    return Mode.ASCEND;
                }
                if(id.equals("teleportermod:descend")) {
                    return Mode.DESCEND;
                }
            }
        }
        return Mode.NORMAL;
    }

    private TypedActionResult<ItemStack> ascendEntity(World world, PlayerEntity playerEntity, Hand hand, boolean hasAscendEnchantment) {
        boolean searchAir = false;
        int tickEffect = 20;
        Vec3d pos = playerEntity.getPos();
        for(int i=(int)pos.getY(); i < 255; i++) {
            BlockState blockState = world.getBlockState(new BlockPos(pos));
            if (Objects.nonNull(blockState)) {
                pos = pos.add(0, 1, 0);
                if (blockState.getBlock() == Blocks.BEDROCK) { // if reached bedrock in nether, cancel
                    playerEntity.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
                    return new TypedActionResult<>(ActionResult.FAIL, playerEntity.getStackInHand(hand));
                }
                boolean isAirBlock = blockState.getMaterial() == Material.AIR;
                if(isAirBlock) { // if not air block in the first place, dont do anything and keep going up
                    // we save teleportation point here
                    if (searchAir) {
                        pos = pos.add(0, -1, 0);
                        break;
                    }
                } else { //solid ground has been found, search for air now
                    searchAir = true;
                }
            }
        }

        if (!searchAir) {// solid block is not found
            if(hasAscendEnchantment) {
                playerEntity.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
                return new TypedActionResult<>(ActionResult.FAIL, playerEntity.getStackInHand(hand));
            }
            pos = playerEntity.getPos(); // reset position
            pos = pos.add(0, 10, 0);
            tickEffect = 70; // increase duration
        }

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        requestTeleport(world, playerEntity, x, y+0.5, z);
        if(!hasAscendEnchantment)
            giveEntityEffect(playerEntity, tickEffect);
        playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        // add item damage
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        itemStack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(hand));

        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    private TypedActionResult<ItemStack> descendEntity(World world, PlayerEntity playerEntity, Hand hand) {
        BlockPos groundPos = playerEntity.getBlockPos().add(0, -1, 0);
        Vec3d teleportingPos = null;
        boolean searchGround = false;
        for(int i=groundPos.getY(); i>0; i--) {
            groundPos = groundPos.add(0, -1, 0);
            BlockState groundBlock = world.getBlockState(groundPos);
            if (groundBlock.getMaterial() == Material.AIR) {
                searchGround = true;
            } else if (searchGround) {
                teleportingPos = new Vec3d(playerEntity.getX(), groundPos.getY() + 1, playerEntity.getZ());
                break;
            }
        }
        if (Objects.nonNull(teleportingPos)) {
            requestTeleport(world, playerEntity, teleportingPos.getX(), teleportingPos.getY()+0.5, teleportingPos.getZ());
            playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
        }
        playerEntity.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 1.0F, 1.0F);
        return new TypedActionResult<>(ActionResult.FAIL, playerEntity.getStackInHand(hand));
    }

    private static void requestTeleport(World world, PlayerEntity playerEntity, double x, double y, double z) {
        Teleportations.teleport(playerEntity, x, y, z);
    }

    @Override
    public int getEnchantability() {
        return 15;
    }
}
