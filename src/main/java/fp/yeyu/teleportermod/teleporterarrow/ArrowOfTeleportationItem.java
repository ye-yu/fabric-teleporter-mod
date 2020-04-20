package fp.yeyu.teleportermod.teleporterarrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ArrowOfTeleportationItem extends ArrowItem {
    public ArrowOfTeleportationItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new ArrowOfTeleportationItemEntity(world, shooter);
    }
}
