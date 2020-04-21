package fp.yeyu.teleportermod.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;

public class TeleporterWand extends FishingRodItem {
    public static final String MOD_NAME = "teleporter_wand";

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
        if (!playerEntity.onGround) {
            // do nothing
            return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
        }

        boolean searchAir = false;
        int tickEffect = 20;
        Vec3d pos = playerEntity.getPos();
        for(int i=(int)pos.getY(); i < 255; i++) {
            BlockState blockState = world.getBlockState(new BlockPos(pos));
            if (Objects.nonNull(blockState)) {
                pos = pos.add(0, 1, 0);
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

        if (!searchAir) { // solid block is not found
            pos = playerEntity.getPos(); // reset position
            pos = pos.add(0, 10, 0);
            tickEffect = 70; // increase duration
        }

        double x = pos.getX(), y = pos.getY(), z = pos.getZ();
        playerEntity.requestTeleport(x, y+0.5, z);
        giveEntityEffect(playerEntity, tickEffect);
        playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        // add item damage
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        itemStack.damage(1, playerEntity, (p) -> p.sendToolBreakStatus(hand));

        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

}
