package net.calibermc.secretblocks.blocks;

import net.calibermc.secretblocks.SecretBlocksClient;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OneWayGlassBlock extends SolidBlock {

	public OneWayGlassBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.isClient) {
			MinecraftClient client = MinecraftClient.getInstance();
			SecretBlocksClient.sendHitSetter(pos, (BlockHitResult) client.crosshairTarget, true);
		}
	}

}
