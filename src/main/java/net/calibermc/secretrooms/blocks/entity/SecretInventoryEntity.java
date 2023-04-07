package net.calibermc.secretrooms.blocks.entity;

import net.calibermc.secretrooms.SecretRooms;
import net.calibermc.secretrooms.gui.SecretInventoryScreenHandler;
import net.calibermc.secretrooms.util.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
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

public class SecretInventoryEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

	public SecretInventoryEntity(BlockPos pos, BlockState state) {
		super(SecretRooms.SECRET_INVENTORY_ENTITY, pos, state);
	}

	//From the ImplementedInventory Interface

	@Override
	public DefaultedList<ItemStack> getItems() {
		return inventory;
	}

	//These Methods are from the NamedScreenHandlerFactory Interface
	//createMenu creates the ScreenHandler itself
	//getDisplayName will Provide its name which is normally shown at the top

	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		//We provide *this* to the screenHandler as our class Implements Inventory
		//Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
		return new SecretInventoryScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public Text getDisplayName() {
		// for earlier versions
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
		// for 1.19+
		//return Text.translatable(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, this.inventory);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.inventory);
//		return nbt;
	}
}
