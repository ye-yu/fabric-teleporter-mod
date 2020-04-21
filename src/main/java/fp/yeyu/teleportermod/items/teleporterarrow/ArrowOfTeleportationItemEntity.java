package fp.yeyu.teleportermod.items.teleporterarrow;

import fp.yeyu.teleportermod.TeleporterMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ArrowOfTeleportationItemEntity extends ArrowEntity {

    public ArrowOfTeleportationItemEntity(EntityType<? extends ArrowOfTeleportationItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowOfTeleportationItemEntity(World world, LivingEntity owner) {
        this(world, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ()); // obtained from ProjectileEntity.java
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = ProjectileEntity.PickupPermission.ALLOWED;
        }
    }

    public ArrowOfTeleportationItemEntity(World world, double x, double y, double z){
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, world);
        this.setPos(x, y, z);

    }
    public ArrowOfTeleportationItemEntity(World world) {
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        LOGGER.info("Arrow hit on block " + hitResult.getType());
    }

    @Override
    protected void onHit(LivingEntity target) {
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, 20, 0);
        target.addStatusEffect(statusEffectInstance);
        LOGGER.info("Arrow hit target " + target.getEntityName());
    }

    protected ItemStack asItemStack() {
        return new ItemStack(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM);
    }

    public void initFromStack(ItemStack stack) { }
}