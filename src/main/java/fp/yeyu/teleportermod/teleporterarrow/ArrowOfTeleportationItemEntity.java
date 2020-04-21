package fp.yeyu.teleportermod.teleporterarrow;

import fp.yeyu.teleportermod.TeleporterMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ArrowOfTeleportationItemEntity extends ProjectileEntity {

    public ArrowOfTeleportationItemEntity(EntityType<? extends ArrowOfTeleportationItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowOfTeleportationItemEntity(World world, LivingEntity owner) {
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, owner, world);
    }

    public ArrowOfTeleportationItemEntity(World world, double x, double y, double z) {
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, x, y, z, world);
    }

    public ArrowOfTeleportationItemEntity(World world) {
        super(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM_ENTITY, world);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, 20, 0);
        target.addStatusEffect(statusEffectInstance);
    }

    protected ItemStack asItemStack() {
        return new ItemStack(TeleporterMod.ARROW_OF_TELEPORTATION_ITEM);
    }
}