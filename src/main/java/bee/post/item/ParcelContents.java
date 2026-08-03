package bee.post.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
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

public record ParcelContents(Optional<ItemStack> letter, Optional<ItemStack> gift, Optional<String> ownerName, Optional<String> targetName) implements TooltipProvider, TooltipComponent {
    public static final ParcelContents EMPTY = new ParcelContents(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    public static final Codec<ParcelContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("letter").forGetter(ParcelContents::letter),
            ItemStack.CODEC.optionalFieldOf("gift").forGetter(ParcelContents::gift),
            Codec.STRING.optionalFieldOf("owner_name").forGetter(ParcelContents::ownerName),
            Codec.STRING.optionalFieldOf("target_name").forGetter(ParcelContents::targetName)
    ).apply(instance, ParcelContents::new));


    @Override
    public void addToTooltip(Item.@NonNull TooltipContext tooltipContext, @NonNull Consumer<Component> consumer, @NonNull TooltipFlag tooltipFlag, @NonNull DataComponentGetter dataComponentGetter) {

        if (Minecraft.getInstance().hasShiftDown() || true) {
            Component unknown = Component.translatable("hud.spamton.unknown");
            if (ownerName.isPresent()) {

                consumer.accept(Component.translatable("hud.spamton.sender").append(Component.literal(ownerName.get())));

            } else {
                consumer.accept(Component.translatable("hud.spamton.sender").append(unknown));
            }

            if (targetName.isPresent()) {

                consumer.accept(Component.translatable("hud.spamton.recipient").append(Component.literal(targetName.get())));

            } else {
                consumer.accept(Component.translatable("hud.spamton.recipient").append(unknown));
            }
        } else {
            MutableComponent key = Component.literal("[").append(Component.translatable(Minecraft.getInstance().options.keyShift.getName()).append("]"));
            MutableComponent hold = Component.literal("Hold ").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);
            MutableComponent info = Component.literal(" for more info").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY);


            consumer.accept(hold.append(key.withStyle(ChatFormatting.RESET)).append(info));

        }


    }
}
