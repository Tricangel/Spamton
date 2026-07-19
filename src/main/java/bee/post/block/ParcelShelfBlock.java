package bee.post.block;

import bee.post.block.entity.ParcelShelfBlockEntity;
import bee.post.registry.MailTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.OptionalInt;

public class ParcelShelfBlock extends BaseEntityBlock implements SelectableSlotContainer {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public ParcelShelfBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (level.getBlockEntity(blockPos) instanceof ParcelShelfBlockEntity blockEntity && !itemStack.isEmpty() && itemStack.is(MailTags.LETTERS)) {
            OptionalInt optionalInt = this.getHitSlot(blockHitResult, blockState.getValue(FACING));
            if (optionalInt.isPresent()) return addItem(blockEntity, itemStack, optionalInt.getAsInt(), player);

        }

        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) instanceof ParcelShelfBlockEntity blockEntity) {
            OptionalInt optionalInt = this.getHitSlot(blockHitResult, blockState.getValue(FACING));
            if (optionalInt.isPresent()) return takeItem(blockEntity, optionalInt.getAsInt(), player);

        }
        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    private static InteractionResult addItem(ParcelShelfBlockEntity blockEntity, ItemStack stack, int slot, @Nullable LivingEntity livingEntity) {
        if (blockEntity.getItem(slot).isEmpty()) {
            blockEntity.setItem(slot, stack.copyWithCount(1));
            stack.consume(1, livingEntity);
            if (livingEntity != null) livingEntity.playSound(SoundEvents.CHISELED_BOOKSHELF_PLACE);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private static InteractionResult takeItem(ParcelShelfBlockEntity blockEntity, int slot, Player player) {
        if (!blockEntity.getItem(slot).isEmpty()) {
            player.addItem(blockEntity.getItem(slot).copyWithCount(1));
            blockEntity.setItem(slot, ItemStack.EMPTY);
            player.playSound(SoundEvents.CHISELED_BOOKSHELF_PICKUP);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ParcelShelfBlock::new);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new ParcelShelfBlockEntity(blockPos, blockState);
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public int getColumns() {
        return 3;
    }

    protected @NonNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    protected @NonNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }
}
