package bee.post.mixin;

import bee.post.block.PackageBlock;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerSlotMixin {


	@Inject(at = @At("HEAD"), method = "mayPlace", cancellable = true)
	private void init(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof PackageBlock) {
			cir.setReturnValue(false);
		}
	}
}