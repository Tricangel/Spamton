package bee.post.item;

import bee.post.block.entity.StampType;
import bee.post.registry.MailItemComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicate;

public record StampPredicate(String name) implements DataComponentPredicate {

    public static final Codec<StampPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("stamp").forGetter(StampPredicate::name)
    ).apply(instance, StampPredicate::new));

    @Override
    public boolean matches(DataComponentGetter dataComponentGetter) {
        String name = dataComponentGetter.getOrDefault(MailItemComponents.STAMP_TYPE, StampType.MOON).getSerializedName();

        return name.equals(this.name);
    }
}
