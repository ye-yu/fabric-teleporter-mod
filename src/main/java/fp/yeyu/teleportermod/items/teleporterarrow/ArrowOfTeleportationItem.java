package fp.yeyu.teleportermod.items.teleporterarrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ArrowOfTeleportationItem extends ArrowItem {
    public static final String MOD_NAME = "arrow_of_teleportation";

    public ArrowOfTeleportationItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        final ArrowOfTeleportationItemEntity arrowOfTeleportationItemEntity = new ArrowOfTeleportationItemEntity(world, shooter);
        final Hand handPossiblyHolding = ProjectileUtil.getHandPossiblyHolding(shooter, Items.BOW);
        final ItemStack bowInHand = shooter.getStackInHand(handPossiblyHolding);
        final ListTag enchantments = bowInHand.getEnchantments();
        for(int i=0;i <enchantments.size();i++) {
            final CompoundTag compound = enchantments.getCompound(i);
            final String id = compound.getString("id");
            if(id.equals("minecraft:punch")) {
                final int lvl = compound.getInt("lvl");
                arrowOfTeleportationItemEntity.setTpMultiplier(lvl + 1);
                break;
            }
        }
        return arrowOfTeleportationItemEntity;
    }
}
