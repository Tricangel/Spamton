package bee.post.mixin;

import bee.post.block.entity.PackageBlockEntity;
import bee.post.registry.MailItemComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {


	@Shadow
	@Final
	public Container container;


	@Inject(at = @At("HEAD"), method = "mayPlace", cancellable = true)
	private void init(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {

		if (container instanceof BaseContainerBlockEntity) {
			boolean hasPackage = false;
			boolean isEmpty = true;

			//if empty allow taped packages
			//if contains taped packages do not allow items
			//allow untaped packages

			for (int i = 0; i < container.getContainerSize(); i++) {
				ItemStack stack = container.getItem(i);
				if (stack.getOrDefault(MailItemComponents.TAPED, false)) {
					hasPackage = true;
				}
				if (!stack.isEmpty()) {
					isEmpty = false;
				}
			}

			if (!isEmpty) {
				boolean itemTaped = itemStack.getOrDefault(MailItemComponents.TAPED, false);
				if (itemTaped || hasPackage) {
					cir.setReturnValue(false);
				}
			}

		}
		if (this.container instanceof PackageBlockEntity && itemStack.is(ItemTags.SHULKER_BOXES)) {
			cir.setReturnValue(!itemStack.getOrDefault(MailItemComponents.TAPED, false));
		}
	}
}