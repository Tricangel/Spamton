package bee.post.mixin;

import bee.post.block.PackageBlock;
import bee.post.block.entity.PackageBlockEntity;
import bee.post.registry.MailItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Horse.class)
public class HorseMixin {

	@Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
	private void init(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {

		if (player.getItemInHand(interactionHand).is(MailItems.STAMP) && player.isCrouching()) {
			cir.setReturnValue(InteractionResult.PASS);
		}

	}
}