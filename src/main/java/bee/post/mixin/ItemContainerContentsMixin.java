package bee.post.mixin;

import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(ItemContainerContents.class)
public class ItemContainerContentsMixin {

	@Inject(at = @At("HEAD"), method = "addToTooltip", cancellable = true)
	private void init(Item.TooltipContext tooltipContext, Consumer<Component> consumer, TooltipFlag tooltipFlag, DataComponentGetter dataComponentGetter, CallbackInfo ci) {

		if (dataComponentGetter.get(MailItemComponents.POSTAL_INFO) != null) {

			ci.cancel();

		}

	}
}