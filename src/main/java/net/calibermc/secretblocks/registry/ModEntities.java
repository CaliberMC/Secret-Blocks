package net.calibermc.secretblocks.registry;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.blocks.entity.SecretBlockEntity;
import net.calibermc.secretblocks.blocks.entity.SecretInventoryEntity;
import net.calibermc.secretblocks.items.SwitchProbe;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static net.calibermc.secretblocks.SecretBlocks.id;

public class ModEntities {
    public static BlockEntityType<SecretBlockEntity> SECRET_BLOCK_ENTITY;
    public static BlockEntityType<SecretInventoryEntity> SECRET_INVENTORY_ENTITY;

    // Registry
    public static void registerAllEntities() {
        SECRET_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                id("secret_block_entity_type"), FabricBlockEntityTypeBuilder.create(SecretBlockEntity::new,
                        ModBlocks.secretBlocksList).build());

//		SECRET_INVENTORY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
//		        id("secret_inventory_entity_type"), FabricBlockEntityTypeBuilder.create(SecretInventoryEntity::new,
//		                inventoryBlocksList).build());

        //Console Output
        System.out.println("Registering Entities for " + SecretBlocks.MOD_ID);
    }


//    // Console Output
//    public static void registerItems() {
//        System.out.println("Registering Items for " + SecretBlocks.MOD_ID);
//    }


}

