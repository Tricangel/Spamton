package bee.post.registry;

import bee.post.Spamton;
import bee.post.item.StampFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;

public class MailLootFunctions {

    public static final LootItemFunctionType<StampFunction> STAMP_FUNCTION = register("stamp_function", StampFunction.CODEC);

    public static <T extends LootItemFunction> LootItemFunctionType<T> register(String name, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Spamton.id(name), new LootItemFunctionType<T>(codec));
    }

    public static void init() {}

}
