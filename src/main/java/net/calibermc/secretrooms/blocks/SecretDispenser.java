package net.calibermc.secretrooms.blocks;

import net.calibermc.secretrooms.SecretRooms;
import net.calibermc.secretrooms.SecretRoomsClient;
import net.calibermc.secretrooms.blocks.entity.CamoBlockEntity;
import net.calibermc.secretrooms.blocks.entity.SecretInventoryEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SecretDispenser extends net.minecraft.block.DispenserBlock implements BlockEntityProvider, CamoBlock {
// extends BlockWithEntity
    public SecretDispenser(Settings settings) { super(settings); }

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 1.0d);
/*
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SecretInventoryEntity(pos, state);
    }
*/
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CamoBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient) {
            MinecraftClient client = MinecraftClient.getInstance();
            SecretRoomsClient.sendHitSetter(pos, (BlockHitResult) client.crosshairTarget, false);
            SecretRoomsClient.sendHitSetter(pos.up(), (BlockHitResult) client.crosshairTarget, false);
        }
    }

   @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
       if (world.getBlockEntity(pos) instanceof CamoBlockEntity) {
           CamoBlockEntity blockEntity = (CamoBlockEntity) world.getBlockEntity(pos);
           ItemStack itemStack = player.getStackInHand(hand);
           if (itemStack.getItem() == Items.HONEYCOMB) {
               if (!blockEntity.waxed) {
                   blockEntity.waxed = true;
                   player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                   return ActionResult.SUCCESS;
               }
           }
       }
       return super.onUse(state, world, pos, player, hand, hit);
   }
/*
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            //This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
            //a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                //With this call the server will request the client to open the appropriate Screenhandler
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    //This method will drop all items onto the ground when the block is broken
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SecretInventoryEntity) {
                ItemScatterer.spawn(world, pos, (SecretInventoryEntity) blockEntity);
                // update comparators
                world.updateComparators(pos,this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
*/
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return (stateFrom.getBlock() instanceof CamoBlock) ? true : super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

}

