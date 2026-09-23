package bee.post.item;

import bee.post.item.component.ParcelContents;
import bee.post.item.component.PostalInfoComponent;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Prediction;
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
        ParcelContents contents = stack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);
        PostalInfoComponent postalInfo = stack.getOrDefault(MailItemComponents.POSTAL_INFO, PostalInfoComponent.EMPTY);
        if (player.isCrouching()) {

            if (postalInfo.ownerName().isEmpty() || !postalInfo.ownerName().get().equals(player.getDisplayName().getString())) {
                postalInfo = new PostalInfoComponent(Optional.of(player.getDisplayName().getString()), postalInfo.targetName());

                stack.set(MailItemComponents.POSTAL_INFO, postalInfo);

                player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
                return InteractionResult.SUCCESS;
            }
        } else {


            if (contents.gift().isPresent()) {
                player.drop(contents.gift().get(), true, Prediction.PREDICTED);
                ParcelContents newContents = new ParcelContents(contents.letter(), Optional.empty());
                saveContents(stack, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            } else if (contents.letter().isPresent()) {
                player.drop(contents.letter().get(), true, Prediction.PREDICTED);
                ParcelContents newContents = new ParcelContents(Optional.empty(), Optional.empty());
                saveContents(stack, newContents);
                player.playSound(SoundEvents.BUNDLE_DROP_CONTENTS);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean overrideStackedOnOther(@NonNull ItemStack itemStack, Slot slot, @NonNull ClickAction clickAction, @NonNull Player player) {
        ParcelContents contents = itemStack.getOrDefault(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY);

        if (!slot.hasItem() && clickAction.equals(ClickAction.SECONDARY)) {

            if (contents.gift().isPresent()) {
                saveContents(itemStack, new ParcelContents(contents.letter(), Optional.empty()));
                slot.set(contents.gift().get());
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

            if (contents.letter().isPresent()) {
                saveContents(itemStack, new ParcelContents(Optional.empty(), Optional.empty()));
                slot.set(contents.letter().get());
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }
        }

        if (slot.hasItem() && clickAction.equals(ClickAction.PRIMARY)) {

            if (slot.getItem().is(MailTags.CANNOT_FIT_IN_PARCELS)) {
                player.playSound(SoundEvents.BUNDLE_INSERT_FAIL);
                return true;
            }

            if (contents.letter().isEmpty() && slot.getItem().is(MailTags.LETTERS)) {
                saveContents(itemStack, new ParcelContents(Optional.of(slot.getItem()), Optional.empty()));
                slot.set(ItemStack.EMPTY);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                return true;
            }

            if (contents.gift().isEmpty() && contents.letter().isPresent()) {
                saveContents(itemStack, new ParcelContents(contents.letter(), Optional.of(slot.getItem())));
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
            PostalInfoComponent postalInfo = itemStack.getOrDefault(MailItemComponents.POSTAL_INFO, PostalInfoComponent.EMPTY);
            postalInfo = new PostalInfoComponent(postalInfo.ownerName(), Optional.of(itemStack2.get(DataComponents.CUSTOM_NAME).getString()));
            itemStack2.shrink(1);
            player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);

            itemStack.set(MailItemComponents.POSTAL_INFO, postalInfo);
            return true;
        }

        if (clickAction.equals(ClickAction.SECONDARY) && itemStack2.isEmpty()) {

            if (contents.gift().isPresent()) {
                itemStack2 = contents.gift().get().copy();
                ParcelContents newContents = new ParcelContents(contents.letter(), Optional.empty());
                saveContents(itemStack, newContents);
                player.playSound(SoundEvents.BUNDLE_REMOVE_ONE);
                slotAccess.set(itemStack2);
                return true;
            } else if (contents.letter().isPresent()) {
                itemStack2 = contents.letter().get().copy();
                ParcelContents newContents = new ParcelContents(Optional.empty(), Optional.empty());
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
                ParcelContents newContents = new ParcelContents(Optional.of(itemStack2.copyWithCount(1)), contents.gift());
                saveContents(itemStack, newContents);
                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            } else if (contents.letter().isPresent() && contents.gift().isEmpty() && !itemStack2.is(MailTags.LETTERS)) {
                ParcelContents newContents = new ParcelContents(contents.letter(), Optional.of(itemStack2.copyWithCount(1)));
                saveContents(itemStack, newContents);

                itemStack2.shrink(1);
                player.playSound(SoundEvents.BUNDLE_INSERT);
                return true;
            }

        }

        return super.overrideOtherStackedOnMe(itemStack, itemStack2, slot, clickAction, player, slotAccess);
    }


    public static void saveContents(ItemStack stack, ParcelContents contents) {
       stack.set(MailItemComponents.PARCEL_CONTENTS, contents);
    }

    @Override
    public @NonNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return itemStack.has(MailItemComponents.PARCEL_CONTENTS) ? Optional.ofNullable(itemStack.get(MailItemComponents.PARCEL_CONTENTS)) : Optional.empty();
    }
}
