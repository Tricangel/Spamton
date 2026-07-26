package bee.post.registry;

import bee.post.item.StampFunction;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class MailLootFunctions {

    public static final MapCodec<StampFunction> STAMP_FUNCTION = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, "stamp_function", StampFunction.CODEC);



    public static void init() {}

}
