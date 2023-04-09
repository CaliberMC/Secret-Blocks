package net.calibermc.secretblocks.registry;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.blocks.entity.SecretBlockEntity;
import net.calibermc.secretblocks.blocks.entity.SecretInventoryEntity;
import net.calibermc.secretblocks.screen.SecretChestScreenHandler;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.calibermc.secretblocks.SecretBlocks.id;

public class ModScreenHandlers {

    //SECRET CHEST
    public static ScreenHandlerType<SecretChestScreenHandler> SECRET_CHEST_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(SecretBlocks.MOD_ID, "secret_chest"),
                    SecretChestScreenHandler::new);



}

