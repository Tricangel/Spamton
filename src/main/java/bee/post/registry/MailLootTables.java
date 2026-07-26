package bee.post.registry;

import bee.post.block.Stamp;
import bee.post.item.StampFunction;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public class MailLootTables {

    public static void insertIntoTables() {

        LootTableEvents.MODIFY.register(((resourceKey, builder, lootTableSource, provider) -> {
            ResourceKey<LootTable> shipwreckMapKey = ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("chests/shipwreck_map"));

            if (shipwreckMapKey.equals(resourceKey)) {


                List<Stamp.StampType> types = List.of(Stamp.StampType.APPLE, Stamp.StampType.MOON, Stamp.StampType.INK, Stamp.StampType.SUN, Stamp.StampType.PICKAXE);

                LootItemFunction appleFunction = StampFunction.setStampTypes(types).build();

                LootPool pool = LootPool.lootPool()
                        .add(LootItem.lootTableItem(MailItems.STAMP).setWeight(15)).apply(appleFunction).setRolls(new ConstantValue(3))
                        .build();

                builder.pool(pool);
            }



        }));


    }

}
