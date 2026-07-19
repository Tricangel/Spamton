package bee.post.item;

import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class ParcelItem extends Item {
    public final DyeColor color;
    public ParcelItem(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        boolean taped = stack.getOrDefault(MailItemComponents.TAPED, false);
        if (player.isCrouching() && !taped) {

            stack.set(MailItemComponents.TAPED, true);
            player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
            return InteractionResult.SUCCESS;
        } else if (!taped){
            ParcelContents contents = loadContents(stack);

            if (!contents.gift().isEmpty()) {
                player.drop(contents.gift(), true);
                ParcelContents newContents = new ParcelContents(contents.letter(), ItemStack.EMPTY);
                saveContents(stack, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            } else if (!contents.letter().isEmpty()) {
                player.drop(contents.letter(), true);
                ParcelContents newContents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY);
                saveContents(stack, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            }
        }

        if (taped && player.isCrouching()) {

            stack.set(MailItemComponents.TAPED, false);
            player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
            return InteractionResult.SUCCESS;

        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack itemStack, ItemStack itemStack2, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        ParcelContents contents = loadContents(itemStack);

        if (itemStack.getOrDefault(MailItemComponents.TAPED, false) && clickAction.equals(ClickAction.SECONDARY) && itemStack2.isEmpty()) {
            itemStack.set(MailItemComponents.TAPED, false);
            player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
            return true;
        }

        if (itemStack.getOrDefault(MailItemComponents.TAPED, false)) return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);

        if (clickAction.equals(ClickAction.SECONDARY) && itemStack2.isEmpty()) {

            if (!contents.gift().isEmpty()) {
                itemStack2 = contents.gift().copy();
                ParcelContents newContents = new ParcelContents(contents.letter(), ItemStack.EMPTY);
                saveContents(itemStack, newContents);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                slotAccess.set(itemStack2);
                return true;
            } else if (!contents.letter().isEmpty()) {
                itemStack2 = contents.letter().copy();
                ParcelContents newContents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY);
                saveContents(itemStack, newContents);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                slotAccess.set(itemStack2);
                return true;
            }

        } else if (!itemStack2.isEmpty()) {

            if (contents.letter().isEmpty() && itemStack2.is(MailTags.LETTERS)) {
                ParcelContents newContents = new ParcelContents(itemStack2.copyWithCount(1), contents.gift());
                saveContents(itemStack, newContents);
                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            } else if (!contents.letter().isEmpty() && contents.gift().isEmpty() && !itemStack2.is(MailTags.LETTERS)) {
                ParcelContents newContents = new ParcelContents(contents.letter(), itemStack2.copyWithCount(1));
                saveContents(itemStack, newContents);

                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

        }

        return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }

    public ParcelContents loadContents(ItemStack stack) {
        ParcelContents contents = stack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY));
        if (contents.gift().is(Items.BEDROCK)) contents = new ParcelContents(contents.letter(), ItemStack.EMPTY);
        if (contents.letter().is(Items.BEDROCK)) contents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY);
        return contents;
    }

    public ItemStack saveContents(ItemStack stack, ParcelContents contents) {
        if (contents.gift().isEmpty()) contents = new ParcelContents(contents.letter(), Items.BEDROCK.getDefaultInstance());
        if (contents.letter().isEmpty()) contents = new ParcelContents(Items.BEDROCK.getDefaultInstance(), Items.BEDROCK.getDefaultInstance());
        stack.set(MailItemComponents.PARCEL_CONTENTS, contents);
        return stack;
    }

}
