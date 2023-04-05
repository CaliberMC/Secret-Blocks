package net.calibermc.secretrooms.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.calibermc.secretrooms.blocks.CamoBlock;
import net.calibermc.secretrooms.blocks.DoorBlock;
import net.calibermc.secretrooms.blocks.TrapdoorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@Environment(EnvType.CLIENT)
@Mixin(Block.class)
public class DrawSideMixin {

	@Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
	private static void TSRshouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> ci) {
		BlockState blockState = world.getBlockState(blockPos);

		if (blockState.getBlock() instanceof CamoBlock) {
			ci.setReturnValue(true);
		}

		if (state.getBlock() == blockState.getBlock() && state.getBlock() instanceof CamoBlock) {
			ci.setReturnValue(false);
		}

		if (state.getBlock() instanceof CamoBlock && (blockState.getBlock() instanceof DoorBlock || blockState.getBlock() instanceof TrapdoorBlock)) {
			ci.setReturnValue(true);
		}
	}

}
