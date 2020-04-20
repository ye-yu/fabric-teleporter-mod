package fp.yeyu.teleportermod;

import fp.yeyu.teleportermod.teleporterarrow.ArrowOfTeleportationItem;
import fp.yeyu.teleportermod.teleporterarrow.ArrowOfTeleportationItemEntity;
import fp.yeyu.teleportermod.teleporterarrow.renderer.ArrowOfTeleportationRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class Teleportermod implements ClientModInitializer {
    public static final String NAMESPACE = "teleportermod";

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

    public static final ArrowItem ARROW_OF_TELEPORTATION_ITEM = new ArrowOfTeleportationItem(new Item
            .Settings()
            .group(ItemGroup.COMBAT)
            .rarity(Rarity.UNCOMMON)
    );

    public static final EntityType<ArrowOfTeleportationItemEntity> ARROW_OF_TELEPORTATION_ITEM_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(NAMESPACE, "arrow_of_teleportation"),
            FabricEntityTypeBuilder
                    .create(EntityCategory.MISC, (EntityType.EntityFactory<ArrowOfTeleportationItemEntity>) ArrowOfTeleportationItemEntity::new)
                    .size(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );

    public void onInitializeClient() {
        // registering teleporter wand
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "teleporter_wand"), TELEPORTER_WAND);

        // registering teleporter plate
        Registry.register(Registry.BLOCK, new Identifier(NAMESPACE, "teleporter_plate"), TELEPORTER_PLATE);
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "teleporter_plate"), TELEPORTER_PLATE_ITEM);

        // registering arrow of teleportation
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "arrow_of_teleportation"), ARROW_OF_TELEPORTATION_ITEM);

        // registering arrow of teleportation client side rendering
        EntityRendererRegistry.INSTANCE.register(ARROW_OF_TELEPORTATION_ITEM_ENTITY, (entityRenderDispatcher, context) -> new ArrowOfTeleportationRenderer(entityRenderDispatcher));
    }
}
