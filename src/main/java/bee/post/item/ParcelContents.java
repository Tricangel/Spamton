package bee.post.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record ParcelContents(ItemStack letter, ItemStack gift) {
    public static final Codec<ParcelContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("letter").forGetter(ParcelContents::letter),
            ItemStack.CODEC.fieldOf("gift").forGetter(ParcelContents::gift)
    ).apply(instance, ParcelContents::new));

}
