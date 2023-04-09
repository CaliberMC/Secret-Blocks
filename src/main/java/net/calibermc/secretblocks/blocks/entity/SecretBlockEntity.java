package net.calibermc.secretblocks.blocks.entity;

import net.calibermc.secretblocks.SecretBlocks;
import net.calibermc.secretblocks.blocks.DoorBlock;
import net.calibermc.secretblocks.blocks.TrapdoorBlock;
import net.calibermc.secretblocks.mixin.AccessibleBakedQuad;
import net.calibermc.secretblocks.mixin.DirectionAccessor;
import net.calibermc.secretblocks.registry.ModEntities;
import net.calibermc.secretblocks.screen.SecretChestScreenHandler;
import net.calibermc.secretblocks.util.ImplementedInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class SecretBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory, BlockEntityTicker<SecretBlockEntity> {

	private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);  // 9 -> 27

	public BlockState upState = Blocks.AIR.getDefaultState();
	public BlockState downState = Blocks.AIR.getDefaultState();
	public BlockState northState = Blocks.AIR.getDefaultState();
	public BlockState eastState = Blocks.AIR.getDefaultState();
	public BlockState southState = Blocks.AIR.getDefaultState();
	public BlockState westState = Blocks.AIR.getDefaultState();

	public Direction upDirection = Direction.UP;
	public Direction downDirection = Direction.DOWN;
	public Direction northDirection = Direction.NORTH;
	public Direction eastDirection = Direction.EAST;
	public Direction southDirection = Direction.SOUTH;
	public Direction westDirection = Direction.WEST;

	public int upRotation = 0;
	public int downRotation = 0;
	public int northRotation = 0;
	public int eastRotation = 0;
	public int southRotation = 0;
	public int westRotation = 0;

	public int age = 0;

	public SecretBlockEntity(BlockPos pos, BlockState state) {
		super(ModEntities.SECRET_BLOCK_ENTITY, pos, state);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, SecretBlockEntity blockEntity) {
		if (age % 120 == 0) {
			refresh();
		}
		age++;
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
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		serialize(tag);
		Inventories.writeNbt(tag, this.inventory);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		deserialize(tag);
		Inventories.readNbt(tag, this.inventory);
		if (this.getWorld() != null && this.getWorld().isClient()) {
			world.updateListeners(pos, null, null, 0);
		}
	}

	public NbtCompound serialize(NbtCompound tag) {

		// Age
		tag.putInt("age", age);

		// States
		tag.put("upState", NbtHelper.fromBlockState(upState));
		tag.put("downState", NbtHelper.fromBlockState(downState));
		tag.put("northState", NbtHelper.fromBlockState(northState));
		tag.put("eastState", NbtHelper.fromBlockState(eastState));
		tag.put("southState", NbtHelper.fromBlockState(southState));
		tag.put("westState", NbtHelper.fromBlockState(westState));

		// Direction
		tag.putString("upDirection", upDirection.getName());
		tag.putString("downDirection", downDirection.getName());
		tag.putString("northDirection", northDirection.getName());
		tag.putString("eastDirection", eastDirection.getName());
		tag.putString("southDirection", southDirection.getName());
		tag.putString("westDirection", westDirection.getName());

		// Rotation
		tag.putInt("upRotation", upRotation);
		tag.putInt("downRotation", downRotation);
		tag.putInt("northRotation", northRotation);
		tag.putInt("eastRotation", eastRotation);
		tag.putInt("southRotation", southRotation);
		tag.putInt("westRotation", westRotation);

		return tag;
	}

	public void deserialize(NbtCompound tag) {

		// Age
		if (tag.contains("age")) {
			this.age = tag.getInt("age");
		}

		// States
		if (tag.contains("upState")) {
			this.upState = NbtHelper.toBlockState(tag.getCompound("upState"));
		}
		if (tag.contains("downState")) {
			this.downState = NbtHelper.toBlockState(tag.getCompound("downState"));
		}
		if (tag.contains("northState")) {
			this.northState = NbtHelper.toBlockState(tag.getCompound("northState"));
		}
		if (tag.contains("eastState")) {
			this.eastState = NbtHelper.toBlockState(tag.getCompound("eastState"));
		}
		if (tag.contains("southState")) {
			this.southState = NbtHelper.toBlockState(tag.getCompound("southState"));
		}
		if (tag.contains("westState")) {
			this.westState = NbtHelper.toBlockState(tag.getCompound("westState"));
		}

		// Direction
		if (tag.contains("upDirection")) {
			upDirection = byName(tag.getString("upDirection"));
		}
		if (tag.contains("downDirection")) {
			downDirection = byName(tag.getString("downDirection"));
		}
		if (tag.contains("northDirection")) {
			northDirection = byName(tag.getString("northDirection"));
		}
		if (tag.contains("eastDirection")) {
			eastDirection = byName(tag.getString("eastDirection"));
		}
		if (tag.contains("southDirection")) {
			southDirection = byName(tag.getString("southDirection"));
		}
		if (tag.contains("westDirection")) {
			westDirection = byName(tag.getString("westDirection"));
		}

		// Rotation
		if (tag.contains("upRotation")) {
			upRotation = tag.getInt("upRotation");
		}
		if (tag.contains("downRotation")) {
			downRotation = tag.getInt("downRotation");
		}
		if (tag.contains("northRotation")) {
			northRotation = tag.getInt("northRotation");
		}
		if (tag.contains("eastRotation")) {
			eastRotation = tag.getInt("eastRotation");
		}
		if (tag.contains("southRotation")) {
			southRotation = tag.getInt("southRotation");
		}
		if (tag.contains("westRotation")) {
			westRotation = tag.getInt("westRotation");
		}
	}

	public void setState(Direction dir, BlockState newState) {

		switch (dir) {
		case UP:
			this.upState = newState;
			break;
		case DOWN:
			this.downState = newState;
			break;
		case NORTH:
			this.northState = newState;
			break;
		case EAST:
			this.eastState = newState;
			break;
		case SOUTH:
			this.southState = newState;
			break;
		case WEST:
			this.westState = newState;
			break;
		}
		update();
	}

	public void setState(BlockState newState) {

		this.upState = newState;
		this.downState = newState;
		this.northState = newState;
		this.eastState = newState;
		this.southState = newState;
		this.westState = newState;
		update();
	}

	public void setDirection(Direction dir, Direction newDir) {

		switch (dir) {
		case UP:
			this.upDirection = newDir;
			break;
		case DOWN:
			this.downDirection = newDir;
			break;
		case NORTH:
			this.northDirection = newDir;
			break;
		case EAST:
			this.eastDirection = newDir;
			break;
		case SOUTH:
			this.southDirection = newDir;
			break;
		case WEST:
			this.westDirection = newDir;
			break;
		}
		update();
	}

	public void setDirection(Direction newDir) {

		this.upDirection = newDir;
		this.downDirection = newDir;
		this.northDirection = newDir;
		this.eastDirection = newDir;
		this.southDirection = newDir;
		this.westDirection = newDir;
		update();
	}

	public BlockState getState(Direction dir) {
		BlockState tempState;
		switch (dir) {
			case DOWN:
			tempState = downState;
			break;
		case NORTH:
			tempState = northState;
			break;
		case EAST:
			tempState = eastState;
			break;
		case SOUTH:
			tempState = southState;
			break;
		case WEST:
			tempState = westState;
			break;
			case UP:
			default:
			tempState = upState;
			break;
		}
		return tempState;
	}

	public static BlockState getState(Direction dir, NbtCompound tag) {
		BlockState tempState = Blocks.STONE.getDefaultState();
		switch (dir) {
			case DOWN:
			if (tag.contains("downState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("downState"));
			}
			break;
		case NORTH:
			if (tag.contains("northState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("northState"));
			}
			break;
		case EAST:
			if (tag.contains("eastState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("eastState"));
			}
			break;
		case SOUTH:
			if (tag.contains("southState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("southState"));
			}
			break;
		case WEST:
			if (tag.contains("westState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("westState"));
			}
			break;
			case UP:
			default:
			if (tag.contains("upState")) {
				tempState = NbtHelper.toBlockState(tag.getCompound("upState"));
			}
			break;
		}
		return tempState;
	}

	public Direction getDir(Direction dir) {
		Direction tempDir;
		switch (dir) {
			case DOWN:
			tempDir = downDirection;
			break;
		case NORTH:
			tempDir = northDirection;
			break;
		case EAST:
			tempDir = eastDirection;
			break;
		case SOUTH:
			tempDir = southDirection;
			break;
		case WEST:
			tempDir = westDirection;
			break;
			case UP:
			default:
			tempDir = upDirection;
			break;
		}
		return tempDir;
	}

	public static Direction getDir(Direction dir, NbtCompound tag) {
		Direction tempDir;
		switch (dir) {
			case DOWN:
			tempDir = byName(tag.getString("downDirection"));
			break;
		case NORTH:
			tempDir = byName(tag.getString("northDirection"));
			break;
		case EAST:
			tempDir = byName(tag.getString("eastDirection"));
			break;
		case SOUTH:
			tempDir = byName(tag.getString("southDirection"));
			break;
		case WEST:
			tempDir = byName(tag.getString("westDirection"));
			break;
			case UP:
			default:
			tempDir = byName(tag.getString("upDirection"));
			break;
		}
		return tempDir;
	}

	public void setRotation(Direction dir, int rotation) {

		switch (dir) {
		case UP:
			this.upRotation = rotation;
			break;
		case DOWN:
			this.downRotation = rotation;
			break;
		case NORTH:
			this.northRotation = rotation;
			break;
		case EAST:
			this.eastRotation = rotation;
			break;
		case SOUTH:
			this.southRotation = rotation;
			break;
		case WEST:
			this.westRotation = rotation;
			break;
		}
		update();
	}

	public void setRotation(int rotation) {

		this.upRotation = rotation;
		this.downRotation = rotation;
		this.northRotation = rotation;
		this.eastRotation = rotation;
		this.southRotation = rotation;
		this.westRotation = rotation;
		update();
	}

	public int getRotation(Direction dir) {
		int tempInt;
		switch (dir) {
		case UP:
			tempInt = upRotation;
			break;
		case DOWN:
			tempInt = downRotation;
			break;
		case NORTH:
			tempInt = northRotation;
			break;
		case EAST:
			tempInt = eastRotation;
			break;
		case SOUTH:
			tempInt = southRotation;
			break;
		case WEST:
			tempInt = westRotation;
			break;
		default:
			tempInt = upRotation;
			break;
		}
		return tempInt;
	}

	@Override
	public void markDirty() {
		super.markDirty();

		if (this.hasWorld() && !this.getWorld().isClient()) {
			((ServerWorld) world).getChunkManager().markForUpdate(getPos());
		}
	}

	public static int getRotation(Direction dir, NbtCompound tag) {
		int tempInt;
		switch (dir) {
			case DOWN:
			tempInt = tag.getInt("downRotation");
			break;
		case NORTH:
			tempInt = tag.getInt("northRotation");
			break;
		case EAST:
			tempInt = tag.getInt("eastRotation");
			break;
		case SOUTH:
			tempInt = tag.getInt("southRotation");
			break;
		case WEST:
			tempInt = tag.getInt("westRotation");
			break;
			case UP:
			default:
			tempInt = tag.getInt("upRotation");
			break;
		}
		return tempInt;
	}

	@Environment(EnvType.CLIENT)
	public void renderBlock(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {

		QuadEmitter emitter = context.getEmitter();

		NbtCompound tag = toInitialChunkDataNbt();

		for (Direction direction : Direction.values()) {

			BlockState tempState = Blocks.STONE.getDefaultState();
			Direction tempDirection = Direction.NORTH;
			int tempRotation = 0;

			if (tag.contains(direction.name().toLowerCase() + "State")) {
				tempState = NbtHelper.toBlockState(tag.getCompound(direction.name().toLowerCase() + "State"));
			}

			if (tag.contains(direction.name().toLowerCase() + "Direction")) {
				tempDirection = byName(tag.getString(direction.name().toLowerCase() + "Direction"));
			}

			if (tag.contains(direction.name().toLowerCase() + "Rotation")) {
				tempRotation = tag.getInt(direction.name().toLowerCase() + "Rotation");
			}

			if (!tempState.isSideSolidFullSquare(blockView, pos, tempDirection)) {
				tempState = Blocks.STONE.getDefaultState();
			}

			BakedModel fullModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(tempState);
			List<BakedQuad> abqList = fullModel.getQuads(tempState, tempDirection, randomSupplier.get());

			BlockColors colors = MinecraftClient.getInstance().getBlockColors();
			int color = 0xFF00_0000 | colors.getColor(tempState, blockView, pos, 0xFF);

			for (BakedQuad quad : abqList) {

				Sprite quadSprite = ((AccessibleBakedQuad) quad).getSprite();

				if (state.getBlock() instanceof DoorBlock) {
					renderDoor(emitter, state, direction);
				} else if (state.getBlock() instanceof TrapdoorBlock) {
					renderTrapdoor(emitter, state, direction);
				} else {
					emitter.square(direction, 0.0f, 0.0f, 1.0f, 1.0f, 0);
				}

				emitter.spriteBake(0, quadSprite, 4);

				rotateAndFlip(emitter, tempRotation, direction);

				if (quad.hasColor()) {
					emitter.colorIndex(0);
					emitter.spriteColor(0, color, color, color, color);
				} else {
					emitter.colorIndex(-1);
					emitter.spriteColor(0, 0xFFFF_FFFF, 0xFFFF_FFFF, 0xFFFF_FFFF, 0xFFFF_FFFF);
				}
				emitter.emit();
			}
		}
	}

	private void renderDoorCuboid(QuadEmitter emitter, Direction faceDirection, Direction cuboidDirection) {
		switch (cuboidDirection) {

		case NORTH:

			if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			}
			break;

		case EAST:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			}
			break;

		case SOUTH:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			}
			break;

		case WEST:

			if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.8125f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 0.1875f, 1.0f, 0.0f);
			}
			break;

		case UP:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.8125f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			}
			break;

		case DOWN:

			if (faceDirection == Direction.SOUTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.NORTH) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.WEST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.EAST) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 0.1875f, 0.0f);
			} else if (faceDirection == Direction.DOWN) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f);
			} else if (faceDirection == Direction.UP) {
				emitter.square(faceDirection, 0.0f, 0.0f, 1.0f, 1.0f, 0.8125f);
			}
			break;
		}
	}

	private void renderDoor(QuadEmitter emitter, BlockState state, Direction direction) {

		Direction renderDirection = Direction.EAST;

		if ((state.get(DoorBlock.FACING) == Direction.NORTH && !state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.EAST && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT && state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.WEST && state.get(DoorBlock.HINGE) == DoorHinge.LEFT && state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.SOUTH;
		} else if ((state.get(DoorBlock.FACING) == Direction.EAST && !state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.SOUTH && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT && state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.NORTH && state.get(DoorBlock.HINGE) == DoorHinge.LEFT && state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.WEST;
		} else if ((state.get(DoorBlock.FACING) == Direction.SOUTH && !state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.WEST && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT && state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.EAST && state.get(DoorBlock.HINGE) == DoorHinge.LEFT && state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.NORTH;
		} else if ((state.get(DoorBlock.FACING) == Direction.WEST && !state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.NORTH && state.get(DoorBlock.HINGE) == DoorHinge.RIGHT && state.get(DoorBlock.OPEN)) || (state.get(DoorBlock.FACING) == Direction.SOUTH && state.get(DoorBlock.HINGE) == DoorHinge.LEFT && state.get(DoorBlock.OPEN))) {
			renderDirection = Direction.EAST;
		} else {
			renderDirection = Direction.NORTH;
		}
		renderDoorCuboid(emitter, direction, renderDirection);
	}

	private void renderTrapdoor(QuadEmitter emitter, BlockState state, Direction direction) {
		if (state.get(TrapdoorBlock.OPEN)) {
			renderDoorCuboid(emitter, direction, state.get(TrapdoorBlock.FACING).getOpposite());
		} else {
			if (state.get(TrapdoorBlock.HALF) == BlockHalf.TOP) {
				renderDoorCuboid(emitter, direction, Direction.UP);
			} else if (state.get(TrapdoorBlock.HALF) == BlockHalf.BOTTOM) {
				renderDoorCuboid(emitter, direction, Direction.DOWN);
			}
		}
	}

	private void rotateAndFlip(QuadEmitter emitter, int rotation, Direction dir) {

		float u0 = emitter.spriteU(0, 0);
		float v0 = emitter.spriteV(0, 0);

		float u1 = emitter.spriteU(1, 0);
		float v1 = emitter.spriteV(1, 0);

		float u2 = emitter.spriteU(2, 0);
		float v2 = emitter.spriteV(2, 0);

		float u3 = emitter.spriteU(3, 0);
		float v3 = emitter.spriteV(3, 0);

		switch (rotation) {
		case 0:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u3, v3);
				emitter.sprite(1, 0, u2, v2);
				emitter.sprite(2, 0, u1, v1);
				emitter.sprite(3, 0, u0, v0);
			} else {
				emitter.sprite(0, 0, u0, v0);
				emitter.sprite(1, 0, u1, v1);
				emitter.sprite(2, 0, u2, v2);
				emitter.sprite(3, 0, u3, v3);
			}
			break;
		case 90:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u0, v0);
				emitter.sprite(1, 0, u3, v3);
				emitter.sprite(2, 0, u2, v2);
				emitter.sprite(3, 0, u1, v1);
			} else {
				emitter.sprite(0, 0, u1, v1);
				emitter.sprite(1, 0, u2, v2);
				emitter.sprite(2, 0, u3, v3);
				emitter.sprite(3, 0, u0, v0);
			}
			break;
		case 180:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u1, v1);
				emitter.sprite(1, 0, u0, v0);
				emitter.sprite(2, 0, u3, v3);
				emitter.sprite(3, 0, u2, v2);
			} else {
				emitter.sprite(0, 0, u2, v2);
				emitter.sprite(1, 0, u3, v3);
				emitter.sprite(2, 0, u0, v0);
				emitter.sprite(3, 0, u1, v1);
			}

			break;
		case 270:
			if (dir.equals(Direction.UP)) {
				emitter.sprite(0, 0, u2, v2);
				emitter.sprite(1, 0, u1, v1);
				emitter.sprite(2, 0, u0, v0);
				emitter.sprite(3, 0, u3, v3);
			} else {
				emitter.sprite(0, 0, u3, v3);
				emitter.sprite(1, 0, u0, v0);
				emitter.sprite(2, 0, u1, v1);
				emitter.sprite(3, 0, u2, v2);
			}

			break;
		}
	}

	public static Direction byName(String name) {
		return name == null ? null : (Direction) DirectionAccessor.NAME_MAP().get(name.toLowerCase(Locale.ROOT));
	}

	public void update() {
		if (!world.isClient) {
			((ServerWorld) world).getChunkManager().markForUpdate(getPos());
		} else {
			MinecraftClient.getInstance();
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public void refresh() {
		if (!world.isClient) {
			for (Direction dir : Direction.values()) {
				SecretBlocks.updateSide(this.getState(dir), dir, pos, this);
				SecretBlocks.updateDirection(this.getDir(dir), dir, pos, this);
				SecretBlocks.updateRotation(this.getRotation(dir), dir, pos, this);
			}
		}
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.createNbt();
	}
}
