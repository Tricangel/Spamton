package bee.post.item;

import bee.post.block.entity.StampType;
import bee.post.registry.MailItemComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class StampItem extends Item {
    public StampItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull Component getName(ItemStack itemStack) {
        if (itemStack.getCustomName() != null) return super.getName(itemStack);

        StampType stampType = itemStack.getOrDefault(MailItemComponents.STAMP_TYPE, StampType.MOON);
        return stampType.getDisplayName();
    }
}
