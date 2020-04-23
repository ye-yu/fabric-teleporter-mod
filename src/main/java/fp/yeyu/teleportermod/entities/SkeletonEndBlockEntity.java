package fp.yeyu.teleportermod.entities;

import fp.yeyu.teleportermod.TeleporterMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Objects;

public class SkeletonEndBlockEntity extends AbstractSkeletonEntity {

    public static final String MOD_NAME = "skeleton";
    private int tickSkeleton = 0;

    public SkeletonEndBlockEntity(EntityType<? extends SkeletonEndBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    public SkeletonEndBlockEntity(World world) {
        super(TeleporterMod.SKELETON_END_BLOCK_ENTITY, world);
    }

    @Override
    public void tick() {
        super.tick();
        BlockState blockBelow = this.world.getBlockState(this.getBlockPos().add(0, -1, 0));
        if (blockBelow.getMaterial() != Material.AIR && blockBelow.getBlock() != Blocks.END_STONE) {
            tickSkeleton++;
        } else {
            tickSkeleton--;
        }

        if (tickSkeleton > 20) {
            convertToSkeleton();
        } else if (tickSkeleton < 0) {
            tickSkeleton = 0;
        }
    }

    private void convertToSkeleton() {
        if (!this.removed) {
            SkeletonEntity skeletonEntity = EntityType.SKELETON.create(this.world);
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
            LOGGER.info("Converted to " + TeleporterMod.SKELETON_END_BLOCK_ID.toString());
        } else {
            LOGGER.info("Skeleton is not spawned.");
        }
    }

    /* copy from SkeletonEntity.java */
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }
}
