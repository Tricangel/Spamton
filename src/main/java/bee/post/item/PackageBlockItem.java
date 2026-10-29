package bee.post.item;

import bee.post.item.component.PostalInfoComponent;
import bee.post.registry.MailItemComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class PackageBlockItem extends BlockItem {
    public PackageBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {

        if (player.isCrouching()) {
            PostalInfoComponent postalInfo = player.getItemInHand(interactionHand).getOrDefault(MailItemComponents.POSTAL_INFO, PostalInfoComponent.EMPTY);

            postalInfo = new PostalInfoComponent(Optional.of(player.getScoreboardName()), postalInfo.targetName());

            player.getItemInHand(interactionHand).set(MailItemComponents.POSTAL_INFO, postalInfo);
            player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);

            return InteractionResult.SUCCESS;


        }

        return super.use(level, player, interactionHand);
    }

}
