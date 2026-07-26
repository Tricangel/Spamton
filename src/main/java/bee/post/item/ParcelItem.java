package bee.post.item;

import bee.post.registry.MailItemComponents;
import bee.post.registry.MailTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class ParcelItem extends Item {
    public final DyeColor color;
    public ParcelItem(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        if (player.isCrouching()) {
            ParcelContents contents = loadContents(stack);

            if (contents.ownerName().isEmpty() || !contents.ownerName().get().equals(player.getDisplayName().getString())) {
                contents = new ParcelContents(contents.letter(), contents.gift(), Optional.of(player.getDisplayName().getString()), contents.targetName());

                saveContents(stack, contents);

                player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
                return InteractionResult.SUCCESS;
            }
        } else {
            ParcelContents contents = loadContents(stack);

            if (!contents.gift().isEmpty()) {
                player.drop(contents.gift(), true);
                ParcelContents newContents = new ParcelContents(contents.letter(), ItemStack.EMPTY, contents.ownerName(), contents.targetName());
                saveContents(stack, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            } else if (!contents.letter().isEmpty()) {
                player.drop(contents.letter(), true);
                ParcelContents newContents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY, contents.ownerName(), contents.targetName());
                saveContents(stack, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean overrideStackedOnOther(@NonNull ItemStack itemStack, Slot slot, @NonNull ClickAction clickAction, @NonNull Player player) {
        if (!slot.hasItem() && clickAction.equals(ClickAction.SECONDARY)) {
            ParcelContents contents = loadContents(itemStack);

            if (!contents.gift().isEmpty()) {
                saveContents(itemStack, new ParcelContents(contents.letter(), ItemStack.EMPTY, contents.ownerName(), contents.targetName()));
                slot.set(contents.gift());
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

            if (!contents.letter().isEmpty()) {
                saveContents(itemStack, new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY, contents.ownerName(), contents.targetName()));
                slot.set(contents.letter());
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }
        }

        if (slot.hasItem() && clickAction.equals(ClickAction.PRIMARY)) {
            ParcelContents contents = loadContents(itemStack);

            if (slot.getItem().is(MailTags.CANNOT_FIT_IN_PARCELS)) {
                player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
                return true;
            }

            if (contents.letter().isEmpty() && slot.getItem().is(MailTags.LETTERS)) {
                saveContents(itemStack, new ParcelContents(slot.getItem(), ItemStack.EMPTY, contents.ownerName(), contents.targetName()));
                slot.set(ItemStack.EMPTY);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                return true;
            }

            if (contents.gift().isEmpty() && !contents.letter().isEmpty()) {
                saveContents(itemStack, new ParcelContents(contents.letter(), slot.getItem(), contents.ownerName(), contents.targetName()));
                slot.set(ItemStack.EMPTY);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                return true;
            }
        }
        return super.overrideStackedOnOther(itemStack, slot, clickAction, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NonNull ItemStack itemStack, @NonNull ItemStack itemStack2, @NonNull Slot slot, ClickAction clickAction, @NonNull Player player, @NonNull SlotAccess slotAccess) {
        ParcelContents contents = loadContents(itemStack);

        if (clickAction.equals(ClickAction.SECONDARY) && itemStack2.is(Items.PAPER) && itemStack2.has(DataComponents.CUSTOM_NAME)) {

            contents = new ParcelContents(contents.letter(), contents.gift(), contents.ownerName(), Optional.of(itemStack2.get(DataComponents.CUSTOM_NAME).getString()));
            itemStack2.shrink(1);
            player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);

            saveContents(itemStack, contents);
            return true;
        }

        if (clickAction.equals(ClickAction.SECONDARY) && itemStack2.isEmpty()) {

            if (!contents.gift().isEmpty()) {
                itemStack2 = contents.gift().copy();
                ParcelContents newContents = new ParcelContents(contents.letter(), ItemStack.EMPTY, contents.ownerName(), contents.targetName());
                saveContents(itemStack, newContents);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                slotAccess.set(itemStack2);
                return true;
            } else if (!contents.letter().isEmpty()) {
                itemStack2 = contents.letter().copy();
                ParcelContents newContents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY, contents.ownerName(), contents.targetName());
                saveContents(itemStack, newContents);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                slotAccess.set(itemStack2);
                return true;
            }

        } else if (clickAction.equals(ClickAction.PRIMARY) && !itemStack2.isEmpty()) {

            if (itemStack2.is(MailTags.CANNOT_FIT_IN_PARCELS)) {
                player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
                return true;
            }

            if (contents.letter().isEmpty() && itemStack2.is(MailTags.LETTERS)) {
                ParcelContents newContents = new ParcelContents(itemStack2.copyWithCount(1), contents.gift(), contents.ownerName(), contents.targetName());
                saveContents(itemStack, newContents);
                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            } else if (!contents.letter().isEmpty() && contents.gift().isEmpty() && !itemStack2.is(MailTags.LETTERS)) {
                ParcelContents newContents = new ParcelContents(contents.letter(), itemStack2.copyWithCount(1), contents.ownerName(), contents.targetName());
                saveContents(itemStack, newContents);

                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

        }

        return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }

    public static ParcelContents loadContents(ItemStack stack) {
        ParcelContents contents = stack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY, Optional.empty(), Optional.empty()));
        if (contents.gift().is(Items.BEDROCK)) contents = new ParcelContents(contents.letter(), ItemStack.EMPTY, contents.ownerName(), contents.targetName());
        if (contents.letter().is(Items.BEDROCK)) contents = new ParcelContents(ItemStack.EMPTY, ItemStack.EMPTY, contents.ownerName(), contents.targetName());
        return contents;
    }

    public static void saveContents(ItemStack stack, ParcelContents contents) {
        if (contents.gift().isEmpty()) contents = new ParcelContents(contents.letter(), Items.BEDROCK.getDefaultInstance(), contents.ownerName(), contents.targetName());
        if (contents.letter().isEmpty()) contents = new ParcelContents(Items.BEDROCK.getDefaultInstance(), Items.BEDROCK.getDefaultInstance(), contents.ownerName(), contents.targetName());
        stack.set(MailItemComponents.PARCEL_CONTENTS, contents);
    }

    @Override
    public @NonNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return itemStack.has(MailItemComponents.PARCEL_CONTENTS) ? Optional.ofNullable(itemStack.get(MailItemComponents.PARCEL_CONTENTS)) : Optional.empty();
    }
}
