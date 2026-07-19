package bee.post.block.entity;

import bee.post.Spamton;
import bee.post.block.PackageBlock;
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

import java.util.ArrayList;
import java.util.List;

public class PackageBlockEntity extends BaseContainerBlockEntity {
    public PackageBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(MailBlockEntities.PACKAGE_BLOCK_ENTITY, blockPos, blockState);
        this.itemStacks = NonNullList.withSize(9, ItemStack.EMPTY);
    }

    private List<PackageBlock.Stamp> stamps = new ArrayList<>(50);
    public NonNullList<ItemStack> itemStacks;



    public boolean addStamp(PackageBlock.Stamp stamp) {
        setChanged();
        if (stamps.size() < 50) {
            stamps.add(stamp);
            return true;
        }
        return false;
    }

    public List<PackageBlock.Stamp> getStamps() {
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
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        itemStacks = nonNullList;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new DispenserMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter dataComponentGetter) {
        super.applyImplicitComponents(dataComponentGetter);

        List<PackageBlock.Stamp> stamps1 = dataComponentGetter.get(MailItemComponents.STAMPS);
        if (stamps1 != null) {
            stamps.addAll(stamps1);
        }


    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.problemPath(), Spamton.LOGGER)) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(scopedCollector, provider);
            tagValueOutput.store("stamps", PackageBlock.Stamp.CODEC.listOf(), stamps);
            return tagValueOutput.buildResult();
        }

    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        if (!this.getBlockState().getValue(PackageBlock.TAPED)) {
            builder.set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        }
        builder.set(MailItemComponents.STAMPS, stamps);

        builder.set(MailItemComponents.TAPED, getBlockState().getValue(PackageBlock.TAPED));
        builder.set(MailItemComponents.DAMAGED, getBlockState().getValue(PackageBlock.DAMAGED));
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        valueInput.read("stamps", PackageBlock.Stamp.CODEC.listOf()).ifPresent(stamps::addAll);
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(valueInput, itemStacks);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.store("stamps", PackageBlock.Stamp.CODEC.listOf(), stamps);
        ContainerHelper.saveAllItems(valueOutput, itemStacks);
    }



    @Override
    public void preRemoveSideEffects(BlockPos blockPos, BlockState blockState) {
        if (!blockState.getValue(PackageBlock.TAPED)) {
            super.preRemoveSideEffects(blockPos, blockState);
            for (PackageBlock.Stamp stamp : stamps) {

                ItemStack stack = MailItems.STAMP.getDefaultInstance();

                stack.set(MailItemComponents.STAMP_TYPE, stamp.stampType());

                stamp.profile().ifPresent(profile -> stack.set(DataComponents.PROFILE, profile));

                Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);

            }
        }
    }
}
