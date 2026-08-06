package bee.post.item;

import bee.post.networking.ClientboundOpenWritableLetterPacket;
import bee.post.networking.ClientboundOpenWrittenLetterPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class LetterItem extends Item {
    public LetterItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand interactionHand) {
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.FAIL;

        ItemStack letter = player.getItemInHand(interactionHand);
        int hand = interactionHand == InteractionHand.MAIN_HAND ? 0 : 1;
        WrittenBookContent content = letter.get(DataComponents.WRITTEN_BOOK_CONTENT);

        if (content != null){
            ClientboundOpenWrittenLetterPacket openScreen = new ClientboundOpenWrittenLetterPacket(content, hand);
            ServerPlayNetworking.send(serverPlayer, openScreen);
        } else {
            ClientboundOpenWritableLetterPacket openScreen = new ClientboundOpenWritableLetterPacket(letter.getOrDefault(DataComponents.WRITABLE_BOOK_CONTENT, new WritableBookContent(List.of())), hand);
            ServerPlayNetworking.send(serverPlayer, openScreen);
        }


        return InteractionResult.SUCCESS;


    }

}
