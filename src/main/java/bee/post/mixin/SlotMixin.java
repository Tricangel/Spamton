package bee.post.mixin;

import bee.post.block.PackageBlock;
import bee.post.block.entity.PackageBlockEntity;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {


	@Shadow
	@Final
	public Container container;

	@Shadow
	@Final
	private int slot;

	@Inject(at = @At("HEAD"), method = "mayPlace", cancellable = true)
	private void init(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof PackageBlock && container instanceof BaseContainerBlockEntity) {
			cir.setReturnValue(false);
		}
		if (this.container instanceof PackageBlockEntity && itemStack.is(ItemTags.SHULKER_BOXES)) {
			cir.setReturnValue(false);
		}
	}
}