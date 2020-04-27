package fp.yeyu.teleportermod.enchantments;

import fp.yeyu.teleportermod.items.TeleporterWand;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public abstract class TeleporterWandEnchantment extends Enchantment {

    protected TeleporterWandEnchantment(Weight weight, EquipmentSlot[] slotTypes) {
        super(weight, EnchantmentTarget.ALL, slotTypes);
    }

    @Override
    public int getMinimumPower(int level) {
        return 5;
    }

    @Override
    public int getMaximumLevel() {
        return 1;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof TeleporterWand;
    }
}