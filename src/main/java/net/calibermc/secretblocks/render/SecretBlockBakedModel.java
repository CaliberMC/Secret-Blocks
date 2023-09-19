package net.calibermc.secretblocks.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.calibermc.secretblocks.blocks.entity.SecretBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class SecretBlockBakedModel implements FabricBakedModel, BakedModel {



	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, net.minecraft.util.math.random.Random random) {
		if(state==null || face == null) {
			return null;
		}else  {
			return new ArrayList<>();
		}
	}

	@Override
	public boolean useAmbientOcclusion() {
		return true;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getParticleSprite() {
		return MinecraftClient.getInstance().getBlockRenderManager().getModel(Blocks.STONE.getDefaultState()).getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier("minecraft:block/block")).getTransformation();
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<net.minecraft.util.math.random.Random> randomSupplier, RenderContext context) {
		BlockEntity entity = blockView.getBlockEntity(pos);
		if (entity instanceof SecretBlockEntity) {
			((SecretBlockEntity) entity).renderBlock(blockView, state, pos, randomSupplier, context);
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<net.minecraft.util.math.random.Random> randomSupplier, RenderContext context) {

	}




}
