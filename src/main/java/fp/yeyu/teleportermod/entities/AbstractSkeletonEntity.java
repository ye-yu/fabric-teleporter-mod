package fp.yeyu.teleportermod.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Objects;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.AvoidSunlightGoal;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.EscapeSunlightGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

@SuppressWarnings("EntityConstructor")
public abstract class AbstractSkeletonEntity extends HostileEntity implements RangedAttackMob {
    private final BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal = new BowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2D, false) {
        public void stop() {
            super.stop();
            AbstractSkeletonEntity.this.setAttacking(false);
        }

        public void start() {
            super.start();
            AbstractSkeletonEntity.this.setAttacking(true);
        }
    };

    protected AbstractSkeletonEntity(EntityType<? extends AbstractSkeletonEntity> type, World world) {
        super(type, world);
        this.updateAttackType();
    }

    protected void initGoals() {
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0D));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        //noinspection RedundantArrayCreation
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    abstract SoundEvent getStepSound();

    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    public void tickMovement() {
        boolean bl = this.isInDaylight();
        if (bl) {
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.HEAD);
            if (!itemStack.isEmpty()) {
                if (itemStack.isDamageable()) {
                    itemStack.setDamage(itemStack.getDamage() + this.random.nextInt(2));
                    if (itemStack.getDamage() >= itemStack.getMaxDamage()) {
                        this.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
                        this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }

                bl = false;
            }

            if (bl) {
                this.setOnFireFor(8);
            }
        }

        super.tickMovement();
    }

    public void tickRiding() {
        super.tickRiding();
        if (this.getVehicle() instanceof MobEntityWithAi) {
            MobEntityWithAi mobEntityWithAi = (MobEntityWithAi)this.getVehicle();
            this.bodyYaw = mobEntityWithAi.bodyYaw;
        }

    }

    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    public EntityData initialize(IWorld world, LocalDifficulty difficulty, SpawnType spawnType, EntityData entityData, CompoundTag entityTag) {
        if (Objects.isNull(entityData) || Objects.isNull(entityTag))
            return null;
        entityData = super.initialize(world, difficulty, spawnType, entityData, entityTag);
        this.initEquipment(difficulty);
        this.updateEnchantments(difficulty);
        this.updateAttackType();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getClampedLocalDifficulty());
        if (this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localDate = LocalDate.now();
            int i = localDate.get(ChronoField.DAY_OF_MONTH);
            int j = localDate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.equipStack(EquipmentSlot.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getEntitySlotId()] = 0.0F;
            }
        }

        return entityData;
    }

    public void updateAttackType() {
        if (this.world != null && !this.world.isClient) {
            this.goalSelector.remove(this.meleeAttackGoal);
            this.goalSelector.remove(this.bowAttackGoal);
            ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
            if (itemStack.getItem() == Items.BOW) {
                int i = 20;
                if (this.world.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.bowAttackGoal.setAttackInterval(i);
                this.goalSelector.add(4, this.bowAttackGoal);
            } else {
                this.goalSelector.add(4, this.meleeAttackGoal);
            }

        }
    }

    public void attack(LivingEntity target, float f) {
        ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        ProjectileEntity projectileEntity = this.createArrowProjectile(itemStack, f);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - projectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = MathHelper.sqrt(d * d + g * g);
        projectileEntity.setVelocity(d, e + h * 0.20000000298023224D, g, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(projectileEntity);
    }

    protected ProjectileEntity createArrowProjectile(ItemStack arrow, float f) {
        return ProjectileUtil.createArrowProjectile(this, arrow, f);
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        this.updateAttackType();
    }

    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        super.equipStack(slot, stack);
        if (!this.world.isClient) {
            this.updateAttackType();
        }

    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.74F;
    }

    public double getHeightOffset() {
        return -0.6D;
    }
}
