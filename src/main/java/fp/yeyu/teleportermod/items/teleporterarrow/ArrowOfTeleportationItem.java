package fp.yeyu.teleportermod.items.teleporterarrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrowOfTeleportationItem extends ArrowItem {
    public static final Logger LOGGER = LogManager.getLogger(ArrowOfTeleportationItem.class);
    public ArrowOfTeleportationItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        LOGGER.info("Created arrow.");
        return new ArrowOfTeleportationItemEntity(world, shooter);
    }
}
