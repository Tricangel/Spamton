package bee.post.block;

import bee.post.block.entity.PackageBlockEntity;
import bee.post.block.entity.StampType;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PackageBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty TAPED = BooleanProperty.create("taped");
    public static final BooleanProperty DAMAGED = BooleanProperty.create("damaged");
    public PackageBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(TAPED);
        builder.add(DAMAGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext)
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(DAMAGED, blockPlaceContext.getItemInHand().getOrDefault(MailItemComponents.DAMAGED, false))
                .setValue(TAPED, blockPlaceContext.getItemInHand().getOrDefault(MailItemComponents.TAPED, false));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (!blockState.getValue(TAPED)) {
            if (itemStack.is(MailTags.TAPE)) {
                level.setBlock(blockPos, blockState.setValue(TAPED, true), 0);
                player.playSound(SoundEvents.HONEY_BLOCK_HIT);
                itemStack.hurtAndBreak(1, player, interactionHand);


                return InteractionResult.SUCCESS;
            }
        } else if (itemStack.is(MailTags.BOX_CUTTER)) {

            player.playSound(SoundEvents.DECORATED_POT_BREAK);
            blockState = blockState.setValue(TAPED, false);
            level.setBlock(blockPos, blockState, 0);


            if (!itemStack.is(MailTags.SAFE_BOX_CUTTER) && RandomSource.create().nextInt(10) == 0) {
                level.destroyBlock(blockPos, true);
                return InteractionResult.SUCCESS;
            }

            if (blockState.getValue(DAMAGED) && RandomSource.create().nextInt(3) == 0) {
                level.destroyBlock(blockPos, true);
                return InteractionResult.SUCCESS;
            }

            blockState = blockState.setValue(DAMAGED, true);
            level.setBlock(blockPos, blockState, 0);
            return InteractionResult.SUCCESS;
        }



        if (!itemStack.is(MailTags.STAMP)) return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);

        if (!(level.getBlockEntity(blockPos) instanceof PackageBlockEntity packageBlockEntity)) return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);

        Vec3 dist = blockHitResult.getLocation().subtract(blockHitResult.getBlockPos().getCenter());

        Vec3 pos = blockPos.getCenter().add(dist);

        List<Stamp> stamps = packageBlockEntity.getStamps();

        for (Stamp stamp : stamps) {
            if (stamp.pos.distanceTo(dist) < 0.20) return InteractionResult.FAIL;
        }

        Optional<ResolvableProfile> profile = itemStack.has(DataComponents.PROFILE) ? Optional.of(itemStack.get(DataComponents.PROFILE)) :Optional.empty();

        if (packageBlockEntity.addStamp(new Stamp(dist, blockHitResult.getDirection(), RandomSource.create().nextInt(180), itemStack.getOrDefault(MailItemComponents.STAMP_TYPE, StampType.MOON), profile))) {
            level.addParticle(ParticleTypes.DUST_PLUME, pos.x, pos.y, pos.z, 0, 0, 0);
            player.playSound(SoundEvents.HONEY_BLOCK_PLACE);
            itemStack.consume(1, player);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;

    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!player.isCrouching() && level instanceof ServerLevel && level.getBlockEntity(blockPos) instanceof PackageBlockEntity packageBlockEntity && !blockState.getValue(TAPED)) {
            player.openMenu(packageBlockEntity);
            return InteractionResult.SUCCESS;
        }

        if (player.isCrouching() && level.getBlockEntity(blockPos) instanceof PackageBlockEntity packageBlockEntity) {
            List<Stamp> stamps = packageBlockEntity.getStamps();
            Vec3 dist = blockHitResult.getLocation().subtract(blockHitResult.getBlockPos().getCenter());

            if (stamps.removeIf(stamp -> {
                if (stamp.pos.distanceTo(dist) < 0.15f) {
                    ItemStack stack = MailItems.STAMP.getDefaultInstance();
                    stack.set(MailItemComponents.STAMP_TYPE, stamp.stampType);
                    stamp.profile.ifPresent(resolvableProfile -> stack.set(DataComponents.PROFILE, resolvableProfile));
                    player.addItem(stack);
                    return true;
                }
                return false;
            })) {
                return InteractionResult.SUCCESS;
            }

        }

        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (level.getBlockEntity(blockPos) instanceof PackageBlockEntity packageBlockEntity && !blockState.getValue(TAPED)) {
            if (!level.isClientSide() && player.preventsBlockDrops() && !packageBlockEntity.isEmpty()) {
                ItemStack stack = this.asItem().getDefaultInstance();
                stack.applyComponents(packageBlockEntity.collectComponents());
                ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + (double)0.5F, (double)blockPos.getY() + (double)0.5F, (double)blockPos.getZ() + (double)0.5F, stack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }

        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {

        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof PackageBlockEntity packageBlockEntity && !blockState.getValue(TAPED)) {

            builder = builder.withDynamicDrop(Identifier.withDefaultNamespace("contents"), (consumer) -> {
                for(int i = 0; i < packageBlockEntity.getContainerSize(); ++i) {
                    consumer.accept(packageBlockEntity.getItem(i));
                }

            });




        }
        return super.getDrops(blockState, builder);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(PackageBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PackageBlockEntity(blockPos, blockState);
    }


    public record Stamp(Vec3 pos, Direction direction, double rotation, StampType stampType, Optional<ResolvableProfile> profile) {
        public static final Codec<Stamp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Vec3.CODEC.fieldOf("pos").forGetter(Stamp::pos),
                Direction.CODEC.fieldOf("direction").forGetter(Stamp::direction),
                Codec.DOUBLE.fieldOf("rotation").forGetter(Stamp::rotation),
                StampType.CODEC.fieldOf("stamp").forGetter(Stamp::stampType),
                ResolvableProfile.CODEC.optionalFieldOf("profile").forGetter(Stamp::profile)
        ).apply(instance, Stamp::new));
    }

}
