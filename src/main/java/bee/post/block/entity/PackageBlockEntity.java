package bee.post.block.entity;

import bee.post.Spamton;
import bee.post.block.PackageBlock;
import bee.post.block.Stamp;
import bee.post.item.component.PostalInfoComponent;
import bee.post.registry.MailBlockEntities;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class PackageBlockEntity extends BaseContainerBlockEntity {
    public PackageBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MailBlockEntities.PACKAGE_BLOCK_ENTITY, blockPos, blockState);
        this.itemStacks = NonNullList.withSize(9, ItemStack.EMPTY);
        this.info = PostalInfoComponent.EMPTY;
        this.stamps = new ArrayList<>(50);
    }

    public ArrayList<Stamp> stamps;
    public NonNullList<ItemStack> itemStacks;
    public PostalInfoComponent info;



    public boolean addStamp(Stamp stamp) {
        setChanged();
        if (stamps.size() < 50) {
            stamps.add(stamp);
            return true;
        }
        return false;
    }


    public ArrayList<Stamp> getStamps() {
        return stamps;
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.translatable("block.spamton.package");
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return itemStacks;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> nonNullList) {
        itemStacks = nonNullList;
    }

    @Override
    protected @NonNull AbstractContainerMenu createMenu(int i, @NonNull Inventory inventory) {
        return new DispenserMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);

        this.info = dataComponentGetter.getOrDefault(MailItemComponents.POSTAL_INFO, PostalInfoComponent.EMPTY);

        List<Stamp> stamps1 = dataComponentGetter.get(MailItemComponents.STAMPS);
        if (stamps1 != null) {
            stamps.addAll(stamps1);
        }


    }

    @Override
    public @NonNull Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider provider) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.problemPath(), Spamton.LOGGER)) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(scopedCollector, provider);

            NonNullList<Stamp.StampHolder> stamps1 = NonNullList.withSize(50, new Stamp.StampHolder(Optional.empty()));

            for (Stamp stamp : stamps) {
                stamps1.add(new Stamp.StampHolder(Optional.of(stamp)));
            }

            tagValueOutput.store("stamp_holders", Stamp.StampHolder.CODEC.listOf(), stamps1);
            tagValueOutput.store("postal_info", PostalInfoComponent.CODEC, info);
            return tagValueOutput.buildResult();
        }

    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder builder) {
        super.collectImplicitComponents(builder);
        if (!this.getBlockState().getValue(PackageBlock.TAPED)) {
            builder.set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        }
        builder.set(MailItemComponents.STAMPS, stamps);
        builder.set(MailItemComponents.POSTAL_INFO, info);

        builder.set(MailItemComponents.TAPED, getBlockState().getValue(PackageBlock.TAPED));
        builder.set(MailItemComponents.DAMAGED, getBlockState().getValue(PackageBlock.DAMAGED));
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput valueInput) {
        super.loadAdditional(valueInput);
        valueInput.read("stamps", Stamp.CODEC.listOf()).ifPresent(s -> {
            if (!stamps.isEmpty()) {
                stamps.addAll(s);
            }
        });
        valueInput.read("stamp_holders", Stamp.StampHolder.CODEC.listOf()).ifPresent(stampHolders -> {
            stamps.clear();
            for (Stamp.StampHolder stampHolder : stampHolders) {
                stampHolder.stamp().ifPresent(stamp -> stamps.add(stamp));
            }
        });
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        valueInput.read("postal_info", PostalInfoComponent.CODEC).ifPresent(postalInfoComponent -> this.info = postalInfoComponent);
        ContainerHelper.loadAllItems(valueInput, itemStacks);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);

        valueOutput.store("stamps", Stamp.CODEC.listOf(), stamps);
        valueOutput.store("postal_info", PostalInfoComponent.CODEC, info);
        ContainerHelper.saveAllItems(valueOutput, itemStacks);
    }



    @Override
    public void preRemoveSideEffects(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        if (level == null) return;
        if (!blockState.getValue(PackageBlock.TAPED)) {
            super.preRemoveSideEffects(blockPos, blockState);
            for (Stamp stamp : stamps) {

                ItemStack stack = MailItems.STAMP.getDefaultInstance();

                stack.set(MailItemComponents.STAMP_TYPE, stamp.stampType());

                stamp.profile().ifPresent(profile -> stack.set(DataComponents.PROFILE, profile));

                Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);

            }
        }
    }
}
