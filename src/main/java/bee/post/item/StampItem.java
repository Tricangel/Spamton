package bee.post.item;

import bee.post.block.entity.StampType;
import bee.post.registry.MailItemComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
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

        StampType stampType = itemStack.getOrDefault(MailItemComponents.STAMP_TYPE, StampType.MOON);
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

}
