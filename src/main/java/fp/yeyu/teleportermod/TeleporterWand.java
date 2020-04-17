package fp.yeyu.teleportermod;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
        BlockPos blockPos = playerEntity.getBlockPos();
        for(int i=blockPos.getY(); i < 255; i++) {
            blockPos = blockPos.add(0, 1, 0);
            BlockState blockState = world.getBlockState(blockPos);
            if (Objects.nonNull(blockState)) {
                boolean isAirBlock = blockState.getBlock() == Blocks.AIR;
                if(isAirBlock) {
                    // we teleport upwards here
                    if (searchAir) {
                        double x = blockPos.getX(), y = blockPos.getY(), z = blockPos.getZ();
                        playerEntity.requestTeleport(x, y+0.5, z);
                        giveEntityEffect(playerEntity, 20);
                        break;
                    } // else dont do anything and keep going up
                } else { //solid ground has been found, search for air now
                    searchAir = true;
                }
            }
        }

        if (!searchAir) { // solid block is not found
            final Vec3d pos = playerEntity.getPos();
            double x = pos.getX(), y = pos.getY(), z = pos.getZ();
            playerEntity.requestTeleport(x, y+10.5, z);
            giveEntityEffect(playerEntity, 70);
        }

        playerEntity.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);

        // add item damage
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        itemStack.damage(1, playerEntity, (p) -> {
            p.sendToolBreakStatus(hand);
        });
        System.out.println("Is Damageable: " + itemStack.isDamageable());
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

}
