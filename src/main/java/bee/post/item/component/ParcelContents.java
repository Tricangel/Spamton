package bee.post.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.function.Consumer;

public record ParcelContents(Optional<ItemStack> letter, Optional<ItemStack> gift) implements TooltipComponent {
    public static final ParcelContents EMPTY = new ParcelContents(Optional.empty(), Optional.empty());
    public static final Codec<ParcelContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("letter").forGetter(ParcelContents::letter),
            ItemStack.CODEC.optionalFieldOf("gift").forGetter(ParcelContents::gift)
    ).apply(instance, ParcelContents::new));


}
