package net.calibermc.secretblocks.screen;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.screen.SecretChestScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    //SECRET CHEST
    public static ScreenHandlerType<SecretChestScreenHandler> SECRET_CHEST_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(SecretBlocks.MOD_ID, "secret_chest"),
                    SecretChestScreenHandler::new);



}

