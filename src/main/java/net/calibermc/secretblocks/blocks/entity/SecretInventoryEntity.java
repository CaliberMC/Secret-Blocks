package net.calibermc.secretblocks.blocks.entity;

import net.calibermc.secretblocks.screen.SecretChestScreenHandler;
import net.calibermc.secretblocks.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class SecretInventoryEntity extends SecretBlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);  // 9 -> 27

	public SecretInventoryEntity( BlockPos pos, BlockState state) {
		super(pos, state);
	}

	// From the ImplementedInventory Interface
	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}

	// From the NamedScreenHandlerFactory Interface
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		//We provide *this* to the screenHandler as our class Implements Inventory
		//Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
		return new SecretChestScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public Text getDisplayName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
		// for 1.19+
		//return Text.translatable(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		Inventories.readNbt(tag, this.inventory);
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		Inventories.writeNbt(tag, this.inventory);
	}
}
