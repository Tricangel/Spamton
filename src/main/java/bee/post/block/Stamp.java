package bee.post.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public record Stamp(Vec3 pos, Direction direction, Integer rotation, StampType stampType, Optional<ResolvableProfile> profile) {
    public static final Codec<Stamp> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("pos").forGetter(Stamp::pos),
            Direction.CODEC.fieldOf("direction").forGetter(Stamp::direction),
            Codec.INT.fieldOf("rotation").forGetter(Stamp::rotation),
            StampType.CODEC.fieldOf("stamp").forGetter(Stamp::stampType),
            ResolvableProfile.CODEC.optionalFieldOf("profile").forGetter(Stamp::profile)
    ).apply(instance, Stamp::new));

    public static final Stamp EMPTY = new Stamp(null, null, null, null, Optional.empty());
            

    public boolean isEmpty() {
        return this.pos == null && this.direction == null && this.rotation == null && this.stampType == null && this.profile.isEmpty();
    }

    public record StampHolder(Optional<Stamp> stamp) {
        public static final Codec<StampHolder> CODEC = RecordCodecBuilder.create(i -> i.group(Stamp.CODEC.optionalFieldOf("stamp").forGetter(StampHolder::stamp)).apply(i, StampHolder::new));
    }
    
    public enum StampType implements StringRepresentable {
        SUN("sun", Component.translatable("stamp.spamton.sun")),
        MOON("moon", Component.translatable("stamp.spamton.moon")),
        PICKAXE("pickaxe", Component.translatable("stamp.spamton.pickaxe")),
        ALIEN("alien", Component.translatable("stamp.spamton.alien")),
        INK("ink", Component.translatable("stamp.spamton.ink")),
        HORSE("horse", Component.translatable("stamp.spamton.horse")),
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
    
}