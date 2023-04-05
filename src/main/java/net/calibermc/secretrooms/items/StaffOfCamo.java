package net.calibermc.secretrooms.items;

import net.calibermc.secretrooms.SecretRooms;
import net.calibermc.secretrooms.blocks.CamoBlock;
import net.calibermc.secretrooms.blocks.entity.CamoBlockEntity;
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

public class StaffOfCamo extends Item {

	public StaffOfCamo(Settings settings) {
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

		if (block instanceof CamoBlock) {
			if (world.getBlockEntity(blockPos) instanceof CamoBlockEntity) {

				CamoBlockEntity camoBlockEntity = (CamoBlockEntity) world.getBlockEntity(blockPos);

				if (!camoBlockEntity.waxed) {
					if (itemStack.getItem() == SecretRooms.STAFF_OF_CAMO) {
						if (itemStack.hasNbt()) {
							SecretRooms.updateSide(getState(itemStack), dir, blockPos, camoBlockEntity);
							SecretRooms.updateDirection(getDirection(itemStack), dir, blockPos, camoBlockEntity);
						} else {
							BlockState stateAdjacent = camoBlockEntity.getState(dir);
							putStateAndDirection(itemStack, stateAdjacent, dir);
						}
					} else if (itemStack.getItem() == SecretRooms.STAFF_OF_CAMO_ROTATION_MODE) {
						SecretRooms.updateRotation(camoBlockEntity.getRotation(dir) == 270 ? 0 : camoBlockEntity.getRotation(dir) + 90, dir, blockPos, camoBlockEntity);
					}
					camoBlockEntity.refresh();
					return ActionResult.SUCCESS;
				} else {
					return ActionResult.FAIL;
				}
			} else {
				return ActionResult.FAIL;
			}
		} else if (block != Blocks.AIR && itemStack.getItem() != SecretRooms.STAFF_OF_CAMO_ROTATION_MODE) {
			putStateAndDirection(itemStack, blockState, dir);
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.FAIL;
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.isSneaking()) {
			user.setStackInHand(hand, new ItemStack(user.getStackInHand(hand).getItem() == SecretRooms.STAFF_OF_CAMO ? SecretRooms.STAFF_OF_CAMO_ROTATION_MODE : SecretRooms.STAFF_OF_CAMO));
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

		tempDir = CamoBlockEntity.byName(tag.getString("setDirection"));

		return tempDir;
	}

}
