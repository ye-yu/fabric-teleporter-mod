package fp.yeyu.teleportermod.entities;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.function.Predicate;

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

    @Override
    public void attack(LivingEntity target, float f) {
        ProjectileEntity projectileEntity = this.createArrowProjectile(f);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - projectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = MathHelper.sqrt(d * d + g * g);
        projectileEntity.setVelocity(d, e + h * 0.20000000298023224D, g, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(projectileEntity);
    }

    protected ProjectileEntity createArrowProjectile(float f) {
        ArrowOfTeleportationItem arrowItem = (ArrowOfTeleportationItem) TeleporterMod.ARROW_OF_TELEPORTATION_ITEM;
        ProjectileEntity projectileEntity = arrowItem.createArrow(this.world, null, this);
        projectileEntity.applyEnchantmentEffects(this, f);
        return projectileEntity;
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
