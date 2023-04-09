package net.calibermc.secretblocks.registry;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.items.SwitchProbe;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static net.calibermc.secretblocks.SecretBlocks.SECRET_BLOCKS_GROUP;

public class ModItems {

    // Items
    public static final Item SECRET_GOGGLES = registerItem("secret_goggles",
            new Item( new FabricItemSettings().group(SECRET_BLOCKS_GROUP).rarity(Rarity.UNCOMMON)));

    public static final Item SWITCH_PROBE = registerItem("switch_probe",
            new SwitchProbe( new FabricItemSettings().group(SECRET_BLOCKS_GROUP).rarity(Rarity.RARE).maxCount(1)));

    public static final Item SWITCH_PROBE_ROTATION_MODE = registerItem("switch_probe_rotation_mode",
            new SwitchProbe( new FabricItemSettings().group(SECRET_BLOCKS_GROUP).rarity(Rarity.RARE).maxCount(1)));

    public static final Item CAMOUFLAGE_PASTE = registerItem("camouflage_paste",
            new Item( new FabricItemSettings().group(SECRET_BLOCKS_GROUP)));


    // Registry
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SecretBlocks.MOD_ID, name), item);
    }

    // Console Output
    public static void registerItems() {
        System.out.println("Registering Items for " + SecretBlocks.MOD_ID);
    }
}

