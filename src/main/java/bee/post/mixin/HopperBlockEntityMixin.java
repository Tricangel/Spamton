package bee.post.mixin;

import bee.post.registry.MailItemComponents;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @WrapMethod(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z")
    private static boolean addItem(Container container, ItemEntity itemEntity, Operation<Boolean> original) {
        if (!itemEntity.getItem().getOrDefault(MailItemComponents.TAPED, false)) {
            return original.call(container, itemEntity);
        }

        boolean isEmpty = true;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                isEmpty = false;
            }
        }

        if (isEmpty) return original.call(container, itemEntity);

        return false;
    }

    @WrapMethod(method = "tryTakeInItemFromSlot")
    private static boolean tryTakeInItemFromSlot(Hopper hopper, Container container, int x, Direction direction, Operation<Boolean> original) {
        ItemStack stack = container.getItem(x);
        if (!stack.getOrDefault(MailItemComponents.TAPED, false)) {
            return original.call(hopper, container, x, direction);
        }

        boolean isEmpty = true;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack1 = container.getItem(i);
            if (!stack1.isEmpty()) {
                isEmpty = false;
            }
        }

        if (isEmpty) return original.call(hopper, container, x, direction);

        return false;
    }

    @WrapMethod(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;")
    private static ItemStack addItem$container(Container container, Container container2, ItemStack stack, Direction direction, Operation<ItemStack> original) {
        if (stack.getOrDefault(MailItemComponents.TAPED, false)) {
            return stack;
        }

        boolean isEmpty = true;

        for (int i = 0; i < container2.getContainerSize(); i++) {
            ItemStack stack1 = container2.getItem(i);
            if (!stack1.isEmpty()) {
                isEmpty = false;
            }
        }

        if (isEmpty) return original.call(container, container2, stack, direction);

        return stack;
    }

}
