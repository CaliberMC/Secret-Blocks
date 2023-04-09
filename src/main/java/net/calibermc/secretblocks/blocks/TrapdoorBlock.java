package net.calibermc.secretblocks.blocks;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.SecretBlocksClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.calibermc.secretblocks.blocks.entity.SecretBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TrapdoorBlock extends net.minecraft.block.TrapdoorBlock implements BlockEntityProvider, SecretBlock {

	public TrapdoorBlock(Settings settings) {
		super(settings);
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

}
