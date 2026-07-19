package bee.post.item;

import bee.post.client.screen.LetterEditScreen;
import bee.post.client.screen.LetterViewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class LetterItem extends Item {
    public final DyeColor color;
    public LetterItem(Properties properties, DyeColor dyeColor) {
        super(properties);
        this.color = dyeColor;
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand interactionHand) {
        if (!level.isClientSide()) return super.use(level, player, interactionHand);

        ItemStack letter = player.getItemInHand(interactionHand);
        if (letter.has(DataComponents.WRITABLE_BOOK_CONTENT)) {
            Minecraft.getInstance().setScreen(new LetterEditScreen(player, letter, interactionHand, letter.get(DataComponents.WRITABLE_BOOK_CONTENT)));
        } else {
            WrittenBookContent content = letter.get(DataComponents.WRITTEN_BOOK_CONTENT);
            if (content != null){
                Minecraft.getInstance().setScreen(new LetterViewScreen(LetterViewScreen.LetterAccess.fromItem(letter)));
            } else {
                Minecraft.getInstance().setScreen(new LetterEditScreen(player, letter, interactionHand, letter.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(List.of()))));
            }
        }
        return InteractionResult.SUCCESS;


    }

}
