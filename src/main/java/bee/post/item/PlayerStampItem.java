package bee.post.item;

import bee.post.block.entity.StampType;
import bee.post.registry.MailItemComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class PlayerStampItem extends Item {
    public PlayerStampItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull Component getName(ItemStack itemStack) {
        if (itemStack.getCustomName() != null) return super.getName(itemStack);

        StampType stampType = itemStack.getOrDefault(MailItemComponents.STAMP_TYPE, StampType.MOON);
        return stampType.getDisplayName();
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {

        if (player.isCrouching()) {
            player.getItemInHand(interactionHand).set(DataComponents.PROFILE, ResolvableProfile.createUnresolved(player.getUUID()));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {

        Level level = useOnContext.getLevel();

        if (level.getBlockEntity(useOnContext.getClickedPos()) instanceof SkullBlockEntity skullBlockEntity && skullBlockEntity.getOwnerProfile() != null) {
            useOnContext.getItemInHand().set(DataComponents.PROFILE, skullBlockEntity.getOwnerProfile());
        }

        return super.useOn(useOnContext);
    }
}
