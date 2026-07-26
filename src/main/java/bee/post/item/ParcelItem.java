package bee.post.item;

import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
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
            ParcelContents contents = stack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);

            if (contents.ownerName().isEmpty() || !contents.ownerName().get().equals(player.getDisplayName().getString())) {
                contents = new ParcelContents(contents.letter(), contents.gift(), Optional.of(player.getDisplayName().getString()), contents.targetName());

                stack.set(MailItemComponents.PARCEL_CONTENTS, contents);

                player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
                return InteractionResult.SUCCESS;
            }
        } else {
            ParcelContents contents = stack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);

            if (contents.gift().isPresent()) {
                player.drop(contents.gift().get(), true);
                ParcelContents newContents = new ParcelContents(contents.letter(), Optional.empty(), contents.ownerName(), contents.targetName());
                stack.set(MailItemComponents.PARCEL_CONTENTS, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            } else if (!contents.letter().isEmpty()) {
                player.drop(contents.letter().get(), true);
                ParcelContents newContents = new ParcelContents(Optional.empty(), Optional.empty(), contents.ownerName(), contents.targetName());
                stack.set(MailItemComponents.PARCEL_CONTENTS, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean overrideStackedOnOther(@NonNull ItemStack itemStack, Slot slot, @NonNull ClickAction clickAction, @NonNull Player player) {
        if (!slot.hasItem() && clickAction.equals(ClickAction.SECONDARY)) {
            ParcelContents contents = itemStack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);

            if (contents.gift().isPresent()) {
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, new ParcelContents(contents.letter(), Optional.empty(), contents.ownerName(), contents.targetName()));
                slot.set(contents.gift().get());
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

            if (contents.letter().isPresent()) {
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, new ParcelContents(Optional.empty(), Optional.empty(), contents.ownerName(), contents.targetName()));
                slot.set(contents.letter().get());
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }
        }

        if (slot.hasItem() && clickAction.equals(ClickAction.PRIMARY)) {
            ParcelContents contents = itemStack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);

            if (slot.getItem().is(MailTags.CANNOT_FIT_IN_PARCELS)) {
                player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
                return true;
            }

            if (contents.letter().isEmpty() && slot.getItem().is(MailTags.LETTERS)) {
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, new ParcelContents(Optional.of(slot.getItem()), Optional.empty(), contents.ownerName(), contents.targetName()));
                slot.set(ItemStack.EMPTY);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                return true;
            }

            if (contents.gift().isEmpty() && contents.letter().isPresent()) {
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, new ParcelContents(contents.letter(), Optional.of(slot.getItem()), contents.ownerName(), contents.targetName()));
                slot.set(ItemStack.EMPTY);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                return true;
            }
        }
        return super.overrideStackedOnOther(itemStack, slot, clickAction, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NonNull ItemStack itemStack, @NonNull ItemStack itemStack2, @NonNull Slot slot, ClickAction clickAction, @NonNull Player player, @NonNull SlotAccess slotAccess) {
        ParcelContents contents = itemStack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);

        if (clickAction.equals(ClickAction.SECONDARY) && itemStack2.is(Items.PAPER) && itemStack2.has(DataComponents.CUSTOM_NAME)) {

            contents = new ParcelContents(contents.letter(), contents.gift(), contents.ownerName(), Optional.of(itemStack2.get(DataComponents.CUSTOM_NAME).getString()));
            itemStack2.shrink(1);
            player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);

            itemStack.set(MailItemComponents.PARCEL_CONTENTS, contents);
            return true;
        }

        if (clickAction.equals(ClickAction.SECONDARY) && itemStack2.isEmpty()) {

            if (contents.gift().isPresent()) {
                itemStack2 = contents.gift().get().copy();
                ParcelContents newContents = new ParcelContents(contents.letter(), Optional.empty(), contents.ownerName(), contents.targetName());
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, newContents);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                slotAccess.set(itemStack2);
                return true;
            } else if (contents.letter().isPresent()) {
                itemStack2 = contents.letter().get().copy();
                ParcelContents newContents = new ParcelContents(Optional.empty(), contents.gift(), contents.ownerName(), contents.targetName());
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, newContents);
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
                ParcelContents newContents = new ParcelContents(Optional.of(itemStack2.copyWithCount(1)), contents.gift(), contents.ownerName(), contents.targetName());
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, newContents);
                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            } else if (!contents.letter().isEmpty() && contents.gift().isEmpty() && !itemStack2.is(MailTags.LETTERS)) {
                ParcelContents newContents = new ParcelContents(contents.letter(), Optional.of(itemStack2.copyWithCount(1)), contents.ownerName(), contents.targetName());
                itemStack.set(MailItemComponents.PARCEL_CONTENTS, newContents);

                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

        }

        return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }

    @Override
    public @NonNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return itemStack.has(MailItemComponents.PARCEL_CONTENTS) ? Optional.ofNullable(itemStack.get(MailItemComponents.PARCEL_CONTENTS)) : Optional.empty();
    }
}
