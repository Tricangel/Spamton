package bee.post.item;

import bee.post.block.Stamp;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailLootFunctions;
import bee.post.registry.MailLootTables;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class StampFunction extends LootItemConditionalFunction {
    public static final MapCodec<StampFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).and(Stamp.StampType.CODEC.listOf().fieldOf("types").forGetter(stampFunction -> stampFunction.types))
                    .apply(instance, StampFunction::new));
    List<Stamp.StampType> types;
    protected StampFunction(List<LootItemCondition> list, List<Stamp.StampType> types) {
        super(list);
        this.types = types;
    }

    public static LootItemConditionalFunction.Builder<?> setStampTypes(List<Stamp.StampType> types) {
        return simpleBuilder((list) -> new StampFunction(list, types));
    }

    @Override
    public @NonNull LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return MailLootFunctions.STAMP_FUNCTION;
    }

    @Override
    protected @NonNull ItemStack run(ItemStack itemStack, @NonNull LootContext lootContext) {
        itemStack.set(MailItemComponents.STAMP_TYPE, types.get(RandomSource.create().nextInt(types.size())));
        return itemStack;
    }
}
