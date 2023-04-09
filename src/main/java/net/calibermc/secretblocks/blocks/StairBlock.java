package net.calibermc.secretblocks.blocks;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.SecretBlocksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.calibermc.secretblocks.blocks.entity.SecretBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StairBlock extends net.minecraft.block.StairsBlock implements BlockEntityProvider, SecretBlock {

	public StairBlock(BlockState baseBlockState, Settings settings) {
		super(baseBlockState, settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SecretBlockEntity(pos, state);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			SecretBlocksClient.sendHitSetter(pos, (BlockHitResult) client.crosshairTarget, false);
		}
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof SecretBlockEntity) {
			SecretBlockEntity blockEntity = (SecretBlockEntity) world.getBlockEntity(pos);
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return (stateFrom.getBlock() instanceof SecretBlock) ? true : super.isSideInvisible(state, stateFrom, direction);
	}


	@Override  // COLLIDE ONLY WITH STAIR SHAPE
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return (state.get(HALF) == BlockHalf.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_INDICES[this.getShapeIndexIndex(state)]];

	}

	@Override  // OUTLINE FULL CUBE
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.fullCube();

	}

    //Gets the stair shape for collision
	private static final int[] SHAPE_INDICES;  // for stairs shape

	private int getShapeIndexIndex(BlockState state) {
		return ((StairShape)state.get(SHAPE)).ordinal() * 4 + ((Direction)state.get(FACING)).getHorizontal();
	}

	private static StairShape getStairShape(BlockState state, BlockView world, BlockPos pos) {
		Direction direction = (Direction)state.get(FACING);
		BlockState blockState = world.getBlockState(pos.offset(direction));
		if (isStairs(blockState) && state.get(HALF) == blockState.get(HALF)) {
			Direction direction2 = (Direction)blockState.get(FACING);
			if (direction2.getAxis() != ((Direction)state.get(FACING)).getAxis() && isDifferentOrientation(state, world, pos, direction2.getOpposite())) {
				if (direction2 == direction.rotateYCounterclockwise()) {
					return StairShape.OUTER_LEFT;
				}

				return StairShape.OUTER_RIGHT;
			}
		}

		BlockState blockState2 = world.getBlockState(pos.offset(direction.getOpposite()));
		if (isStairs(blockState2) && state.get(HALF) == blockState2.get(HALF)) {
			Direction direction3 = (Direction)blockState2.get(FACING);
			if (direction3.getAxis() != ((Direction)state.get(FACING)).getAxis() && isDifferentOrientation(state, world, pos, direction3)) {
				if (direction3 == direction.rotateYCounterclockwise()) {
					return StairShape.INNER_LEFT;
				}

				return StairShape.INNER_RIGHT;
			}
		}

		return StairShape.STRAIGHT;
	}

	private static boolean isDifferentOrientation(BlockState state, BlockView world, BlockPos pos, Direction dir) {
		BlockState blockState = world.getBlockState(pos.offset(dir));
		return !isStairs(blockState) || blockState.get(FACING) != state.get(FACING) || blockState.get(HALF) != state.get(HALF);
	}

	static {
		SHAPE_INDICES = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};
	}

}