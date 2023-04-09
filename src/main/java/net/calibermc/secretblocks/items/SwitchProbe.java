package net.calibermc.secretblocks.items;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.blocks.SecretBlock;
import net.calibermc.secretblocks.blocks.entity.SecretBlockEntity;
import net.calibermc.secretblocks.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SwitchProbe extends Item {

	public SwitchProbe(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {

		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		ItemStack itemStack = context.getStack();
		Direction dir = context.getSide();

		if (block instanceof SecretBlock) {
			if (world.getBlockEntity(blockPos) instanceof SecretBlockEntity) {

				SecretBlockEntity secretBlockEntity = (SecretBlockEntity) world.getBlockEntity(blockPos);

				if (itemStack.getItem() == ModItems.SWITCH_PROBE) {
					if (itemStack.hasNbt()) {
						SecretBlocks.updateSide(getState(itemStack), dir, blockPos, secretBlockEntity);
						SecretBlocks.updateDirection(getDirection(itemStack), dir, blockPos, secretBlockEntity);
					} else {
						BlockState stateAdjacent = secretBlockEntity.getState(dir);
						putStateAndDirection(itemStack, stateAdjacent, dir);
					}
				} else if (itemStack.getItem() == ModItems.SWITCH_PROBE_ROTATION_MODE) {
					SecretBlocks.updateRotation(secretBlockEntity.getRotation(dir) == 270 ? 0 : secretBlockEntity.getRotation(dir) + 90, dir, blockPos, secretBlockEntity);
				}
				secretBlockEntity.refresh();
				return ActionResult.SUCCESS;

			} else {
				return ActionResult.FAIL;
			}
		} else if (block != Blocks.AIR && itemStack.getItem() != ModItems.SWITCH_PROBE_ROTATION_MODE) {
			putStateAndDirection(itemStack, blockState, dir);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.FAIL;
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			user.setStackInHand(hand, new ItemStack(user.getStackInHand(hand).getItem() == ModItems.SWITCH_PROBE ? ModItems.SWITCH_PROBE_ROTATION_MODE : ModItems.SWITCH_PROBE));
		}
		return super.use(world, user, hand);
	}

	private static void putStateAndDirection(ItemStack itemStack, BlockState state, Direction dir) {
		NbtCompound tag = itemStack.getOrCreateNbt();
		tag.put("setState", NbtHelper.fromBlockState(state));
		tag.putString("setDirection", dir.name().toLowerCase());
	}

	private static BlockState getState(ItemStack itemStack) {
		return NbtHelper.toBlockState(itemStack.getOrCreateSubNbt("setState"));
	}

	private static Direction getDirection(ItemStack itemStack) {
		NbtCompound tag = itemStack.getOrCreateNbt();
		Direction tempDir = Direction.NORTH;

		tempDir = SecretBlockEntity.byName(tag.getString("setDirection"));

		return tempDir;
	}

}
