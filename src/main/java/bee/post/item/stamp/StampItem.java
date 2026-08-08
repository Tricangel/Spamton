package bee.post.item.stamp;

import bee.post.block.Stamp;
import bee.post.registry.MailItemComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jspecify.annotations.NonNull;

public class StampItem extends Item {
    public StampItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull Component getName(ItemStack itemStack) {
        if (itemStack.getCustomName() != null) return super.getName(itemStack);

        if (itemStack.has(DataComponents.PROFILE)) {
            ResolvableProfile profile = itemStack.get(DataComponents.PROFILE);
            return Component.literal("Stamp of " + profile.partialProfile().name());
        }

        Stamp.StampType stampType = itemStack.getOrDefault(MailItemComponents.STAMP_TYPE, Stamp.StampType.MOON);
        return stampType.getDisplayName();
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {

        Level level = useOnContext.getLevel();

        if (level.getBlockEntity(useOnContext.getClickedPos()) instanceof SkullBlockEntity skullBlockEntity && skullBlockEntity.getOwnerProfile() != null) {
            useOnContext.getItemInHand().set(DataComponents.PROFILE, skullBlockEntity.getOwnerProfile());
            useOnContext.getPlayer().playSound(SoundEvents.UI_LOOM_TAKE_RESULT);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(useOnContext);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {

        if (player.isCrouching() && livingEntity instanceof Player target) {
            ItemStack newStack = itemStack.copyWithCount(1);
            itemStack.consume(1, player);
            if (target.getStringUUID().equals("99559de5-392e-443b-9bc5-0a4b60560cbf")) {
                newStack.set(MailItemComponents.STAMP_TYPE, Stamp.StampType.ALIEN);
            } else newStack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(target.getGameProfile()));
            player.addItem(newStack);
            return InteractionResult.SUCCESS;
        }

        if (player.isCrouching() && livingEntity instanceof Horse) {
            ItemStack newStack = itemStack.copyWithCount(1);
            itemStack.consume(1, player);
            newStack.set(MailItemComponents.STAMP_TYPE, Stamp.StampType.HORSE);
            player.addItem(newStack);
            return InteractionResult.SUCCESS;
        }

        return super.interactLivingEntity(itemStack, player, livingEntity, interactionHand);
    }
}
