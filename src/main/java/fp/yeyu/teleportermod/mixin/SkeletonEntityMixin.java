package fp.yeyu.teleportermod.mixin;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.entities.SkeletonEndBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin extends AbstractSkeletonEntity {

    protected SkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> type, World world) {
        super(type, world);
    }

    private int tickSkeleton = 0;

    @Override
    public void tick() {
        super.tick();
        BlockState blockBelow = this.world.getBlockState(this.getBlockPos().add(0, -1, 0));
        if (SkeletonEndBlockEntity.isValidBlock(blockBelow.getBlock())) {
            tickSkeleton++;
        } else {
            tickSkeleton--;
        }

        if (tickSkeleton > 20) {
            convertToSkeletonEndBlock();
        } else if (tickSkeleton < 0) {
            tickSkeleton = 0;
        }
    }

    private void convertToSkeletonEndBlock() {
        if (!this.removed) {
            SkeletonEndBlockEntity skeletonEntity = TeleporterMod.SKELETON_END_BLOCK_ENTITY.create(this.world);
            if (Objects.nonNull(skeletonEntity)) {
                skeletonEntity.copyPositionAndRotation(this);
                skeletonEntity.setCanPickUpLoot(this.canPickUpLoot());
                skeletonEntity.setAiDisabled(this.isAiDisabled());
                EquipmentSlot[] var3 = EquipmentSlot.values();

                for (EquipmentSlot equipmentSlot : var3) {
                    ItemStack itemStack = this.getEquippedStack(equipmentSlot);
                    if (!itemStack.isEmpty()) {
                        skeletonEntity.equipStack(equipmentSlot, itemStack.copy());
                        skeletonEntity.setEquipmentDropChance(equipmentSlot, this.getDropChance(equipmentSlot));
                        itemStack.setCount(0);
                    }
                }

                if (this.hasCustomName()) {
                    skeletonEntity.setCustomName(this.getCustomName());
                    skeletonEntity.setCustomNameVisible(this.isCustomNameVisible());
                }

                if (this.isPersistent()) {
                    skeletonEntity.setPersistent();
                }

                skeletonEntity.setInvulnerable(this.isInvulnerable());
                this.world.spawnEntity(skeletonEntity);
                this.remove();
            }
            LOGGER.info("Converted to " + EntityType.SKELETON.toString());
        } else {
            LOGGER.info("Skeleton is not spawned.");
        }
    }
}
