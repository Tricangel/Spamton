package bee.post.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum StampType implements StringRepresentable {
    SUN("sun", Component.translatable("stamp.spamton.sun")),
    MOON("moon", Component.translatable("stamp.spamton.moon")),
    PICKAXE("pickaxe", Component.translatable("stamp.spamton.pickaxe")),
    APPLE("apple",  Component.translatable("stamp.spamton.apple"));

    final String serializedName;
    final MutableComponent displayName;
    StampType(String serializedName, MutableComponent displayName) {
        this.serializedName = serializedName;
        this.displayName = displayName;
    }

    @Override
    public @NonNull String getSerializedName() {
        return serializedName;
    }

    public static final Codec<StampType> CODEC = StringRepresentable.fromEnum(StampType::values);

    public MutableComponent getDisplayName() {
        return displayName;
    }
}
