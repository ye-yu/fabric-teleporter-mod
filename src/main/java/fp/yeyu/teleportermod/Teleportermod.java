package fp.yeyu.teleportermod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Teleportermod implements ModInitializer {
    public static final TeleporterWand TELEPORTER_WAND = new TeleporterWand((new Item
            .Settings())
            .maxDamage(35)
            .group(ItemGroup.TRANSPORTATION)
            );
    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("teleportermod", "teleporter_wand"), TELEPORTER_WAND);
    }
}
