package fp.yeyu.teleportermod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Teleportermod implements ModInitializer {
    public static final TeleporterWand TELEPORTER_WAND = new TeleporterWand((new Item
            .Settings())
            .maxDamage(35)
            .group(ItemGroup.TRANSPORTATION)
            );

    public static final TeleporterPlate TELEPORTER_PLATE = new TeleporterPlate(PressurePlateBlock.ActivationRule.EVERYTHING,
            FabricBlockSettings
                    .of(Material.WOOD)
                    .noCollision()
                    .hardness(0.5F)
                    .sounds(BlockSoundGroup.WOOD)
                    .build());

    public static final BlockItem TELEPORTER_PLATE_ITEM = new BlockItem(TELEPORTER_PLATE, new Item
            .Settings()
            .group(ItemGroup.TRANSPORTATION)
    );
    @Override
    public void onInitialize() {
        // registering teleporter wand
        Registry.register(Registry.ITEM, new Identifier("teleportermod", "teleporter_wand"), TELEPORTER_WAND);

        // registering teleporter plate
        Registry.register(Registry.BLOCK, new Identifier("teleportermod", "teleporter_plate"), TELEPORTER_PLATE);
        Registry.register(Registry.ITEM, new Identifier("teleportermod", "teleporter_plate"), TELEPORTER_PLATE_ITEM);
    }

}
