package bee.post.item.stamp;

import bee.post.block.Stamp;
import bee.post.registry.MailItemComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class StampFunction extends LootItemConditionalFunction {
    public static final MapCodec<StampFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i)
            .and(Stamp.StampType.CODEC.listOf()
                    .fieldOf("stamps")
                    .forGetter(f -> f.types)
            ).apply(i, StampFunction::new));
    List<Stamp.StampType> types;
    protected StampFunction(Optional<Holder<LootItemCondition>> condition, List<Stamp.StampType> types) {
        super(condition);
        this.types = types;
    }

    public static LootItemConditionalFunction.Builder<?> setStampTypes(List<Stamp.StampType> types) {
        return simpleBuilder((_) -> new StampFunction(Optional.empty(), types));
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected @NonNull ItemStack run(ItemStack itemStack, @NonNull LootContext lootContext) {
        itemStack.set(MailItemComponents.STAMP_TYPE, types.get(RandomSource.create().nextInt(types.size())));
        return itemStack;
    }
}
