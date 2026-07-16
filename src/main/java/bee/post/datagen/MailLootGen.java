package bee.post.datagen;

import bee.post.block.PackageBlock;
import bee.post.registry.MailBlocks;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class MailLootGen extends FabricBlockLootTableProvider {
    public MailLootGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        add(MailBlocks.PACKAGE, createPackageBoxDrop(MailBlocks.PACKAGE, MailItems.CARDBOARD));

    }

    public LootTable.Builder createPackageBoxDrop(Block block, Item item) {
        return LootTable.lootTable()
                .withPool(this.applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block)
                                .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                        .include(DataComponents.CUSTOM_NAME)
                                        .include(DataComponents.CONTAINER)
                                        .include(DataComponents.LOCK)
                                        .include(MailItemComponents.DAMAGED)
                                        .include(MailItemComponents.STAMPS)
                                        .include(MailItemComponents.TAPED))))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MailBlocks.PACKAGE)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PackageBlock.TAPED, true))))
                .withPool(
                        this.applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(4.0F))
                        .add(LootItem.lootTableItem(item)))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(MailBlocks.PACKAGE)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PackageBlock.TAPED, false  )))
                );
    }


}
