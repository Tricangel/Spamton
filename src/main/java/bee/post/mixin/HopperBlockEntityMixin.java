package bee.post.mixin;

import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @WrapMethod(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z")
    private static boolean addItem(Container container, ItemEntity itemEntity, Operation<Boolean> original) {
        if (!itemEntity.getItem().getOrDefault(MailItemComponents.TAPED, false)) {
            return original.call(container, itemEntity);
        }

        return false;
    }

}
