package bee.post.registry;

import bee.post.Spamton;
import bee.post.item.stamp.StampFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public class MailLootFunctions {

    public static final MapCodec<StampFunction> STAMP_FUNCTION = register("stamp_function", StampFunction.CODEC);

    public static <T extends LootItemFunction> MapCodec<T> register(String name, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Spamton.id(name), codec);
    }

    public static void init() {}

}
