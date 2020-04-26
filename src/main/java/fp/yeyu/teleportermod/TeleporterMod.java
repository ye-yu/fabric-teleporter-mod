package fp.yeyu.teleportermod;

import fp.yeyu.teleportermod.blocks.TeleporterPlate;
import fp.yeyu.teleportermod.entities.SkeletonEndBlockEntity;
import fp.yeyu.teleportermod.items.TeleporterWand;
import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItem;
import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItemEntity;
import fp.yeyu.teleportermod.utils.Commands;
import fp.yeyu.teleportermod.utils.Particles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
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

public class TeleporterMod implements ModInitializer {
    public static final String NAMESPACE = "teleportermod";

    public static final Identifier TELEPORTER_WAND_ID = new Identifier(NAMESPACE, TeleporterWand.MOD_NAME);
    public static final Identifier TELEPORTER_PLATE_ID = new Identifier(NAMESPACE, TeleporterPlate.MOD_NAME);
    public static final Identifier ARROW_OF_TELEPORTATION_ID = new Identifier(NAMESPACE, ArrowOfTeleportationItem.MOD_NAME);
    public static final Identifier SKELETON_END_BLOCK_ID = new Identifier(NAMESPACE, SkeletonEndBlockEntity.MOD_NAME);
    public static final Identifier AOT_PARTICLE_ID = Particles.AOT_PARTICLE_ID;
    public static final Identifier TPLATE_PARTICLE_ID = Particles.TPLATE_PARTICLE_ID;
    public static final Identifier REQUEST_TP_ID = new Identifier(NAMESPACE, "request_tp");

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
            ARROW_OF_TELEPORTATION_ID,
            FabricEntityTypeBuilder
                    .create(EntityCategory.MISC, (EntityType.EntityFactory<ArrowOfTeleportationItemEntity>) ArrowOfTeleportationItemEntity::new)
                    .size(EntityDimensions.fixed(0.5f, 0.5f)).build()
    );

    public static final EntityType<SkeletonEndBlockEntity> SKELETON_END_BLOCK_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            SKELETON_END_BLOCK_ID,
            FabricEntityTypeBuilder
                    .create(EntityCategory.AMBIENT, (EntityType.EntityFactory<SkeletonEndBlockEntity>) SkeletonEndBlockEntity::new)
                    .size(EntityDimensions.fixed(0.6F, 1.99F)).build()
    );

    public void onInitialize() {
        // registering teleporter wand
        Registry.register(Registry.ITEM, TELEPORTER_WAND_ID, TELEPORTER_WAND);

        // registering teleporter plate
        Registry.register(Registry.BLOCK, TELEPORTER_PLATE_ID, TELEPORTER_PLATE);
        Registry.register(Registry.ITEM, TELEPORTER_PLATE_ID, TELEPORTER_PLATE_ITEM);

        // registering arrow of teleportation
        Registry.register(Registry.ITEM, ARROW_OF_TELEPORTATION_ID, ARROW_OF_TELEPORTATION_ITEM);

        // registering teleport request id
        ServerSidePacketRegistry.INSTANCE.register(REQUEST_TP_ID, Commands::teleportPacket);
    }

}
