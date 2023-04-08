package net.calibermc.secretrooms;

import io.netty.buffer.Unpooled;
import net.calibermc.secretrooms.gui.SecretInventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.calibermc.secretrooms.blocks.entity.CamoBlockEntity;
import net.calibermc.secretrooms.render.CamoBlockResourceProvider;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class SecretRoomsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {


		HandledScreens.register(SecretRooms.SECRET_INVENTORY_SCREEN_HANDLER, SecretInventoryScreen::new);  //Screen handler
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rm -> new CamoBlockResourceProvider());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), SecretRooms.camoBlocksList);
//		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), SecretRooms.glassBlocksList);

		ClientPlayNetworking.registerGlobalReceiver(SecretRooms.id("update_side"), (client, handler, buf, responseSender) -> {
			NbtCompound tag = buf.readNbt();
			Direction dir = buf.readEnumConstant(Direction.class);
			BlockPos pos = buf.readBlockPos();
			BlockState state = NbtHelper.toBlockState(tag.getCompound("state"));
			client.execute(() -> {
				CamoBlockEntity camoBlockEntity = ((CamoBlockEntity) client.world.getBlockEntity(pos));
				if (camoBlockEntity != null) {
					camoBlockEntity.setState(dir, state);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(SecretRooms.id("update_direction"), (client, handler, buf, responseSender) -> {
			Direction faceDir = buf.readEnumConstant(Direction.class);
			Direction dir = buf.readEnumConstant(Direction.class);
			BlockPos pos = buf.readBlockPos();
			client.execute(() -> {
				CamoBlockEntity camoBlockEntity = ((CamoBlockEntity) client.world.getBlockEntity(pos));
				if (camoBlockEntity != null) {
					camoBlockEntity.setDirection(dir, faceDir);
				}
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(SecretRooms.id("update_rotation"), (client, handler, buf, responseSender) -> {
			int rotation = buf.readInt();
			Direction dir = buf.readEnumConstant(Direction.class);
			BlockPos pos = buf.readBlockPos();
			client.execute(() -> {
				CamoBlockEntity camoBlockEntity = ((CamoBlockEntity) client.world.getBlockEntity(pos));
				if (camoBlockEntity != null) {
					camoBlockEntity.setRotation(dir, rotation);
				}
			});
		});

	}

	public static void sendHitSetter(BlockPos pos, BlockHitResult hit, boolean glass) {
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(pos);
		passedData.writeBlockHitResult(hit);
		passedData.writeBoolean(glass);
		ClientPlayNetworking.send(SecretRooms.id("hit_setter"), passedData);
	}


}
