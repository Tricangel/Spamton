package bee.post.datagen;

import bee.post.registry.MailBlocks;
import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class MailLangGen extends FabricLanguageProvider {
    public MailLangGen(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {

        translationBuilder.add(MailBlocks.PACKAGE, "Package");
        translationBuilder.add(MailBlocks.PARCEL_SHELF, "Parcel Shelf");

        translationBuilder.add(MailItems.STAMP, "Stamp");
        translationBuilder.add(MailItems.BOX_CUTTER, "Box Cutter");
        translationBuilder.add(MailItems.TAPE, "Tape");
        translationBuilder.add(MailItems.CARDBOARD, "Cardboard");
        translationBuilder.add(MailItems.LETTER, "Letter");

        translationBuilder.add(MailItems.WHITE_PARCEL, "White Parcel");
        translationBuilder.add(MailItems.ORANGE_PARCEL, "Orange Parcel");
        translationBuilder.add(MailItems.MAGENTA_PARCEL, "Magenta Parcel");
        translationBuilder.add(MailItems.LIGHT_BLUE_PARCEL, "Light Blue Parcel");
        translationBuilder.add(MailItems.YELLOW_PARCEL, "Yellow Parcel");
        translationBuilder.add(MailItems.LIME_PARCEL, "Lime Parcel");
        translationBuilder.add(MailItems.PINK_PARCEL, "Pink Parcel");
        translationBuilder.add(MailItems.GRAY_PARCEL, "Gray Parcel");
        translationBuilder.add(MailItems.LIGHT_GRAY_PARCEL, "Light Gray Parcel");
        translationBuilder.add(MailItems.CYAN_PARCEL, "Cyan Parcel");
        translationBuilder.add(MailItems.PURPLE_PARCEL, "Purple Parcel");
        translationBuilder.add(MailItems.BLUE_PARCEL, "Blue Parcel");
        translationBuilder.add(MailItems.BROWN_PARCEL, "Brown Parcel");
        translationBuilder.add(MailItems.GREEN_PARCEL, "Green Parcel");
        translationBuilder.add(MailItems.RED_PARCEL, "Red Parcel");
        translationBuilder.add(MailItems.BLACK_PARCEL, "Black Parcel");

        translationBuilder.add("stamp.spamton.moon", "Moon Stamp");
        translationBuilder.add("stamp.spamton.sun", "Sun Stamp");
        translationBuilder.add("stamp.spamton.pickaxe", "Pickaxe Stamp");
        translationBuilder.add("stamp.spamton.alien", "Alien Stamp");
        translationBuilder.add("stamp.spamton.horse", "Horse Stamp");
        translationBuilder.add("stamp.spamton.ink", "Ink Stamp");
        translationBuilder.add("stamp.spamton.apple", "Apple Stamp");

        translationBuilder.add(MailTags.STAMP, "Stamps");
        translationBuilder.add(MailTags.LETTERS, "Can Fit In Parcel First Slot");
        translationBuilder.add(MailTags.TAPE, "Tape");
        translationBuilder.add(MailTags.BOX_CUTTER, "Box Cutters");
        translationBuilder.add(MailTags.SAFE_BOX_CUTTER, "Safe Box Cutters");
        translationBuilder.add(MailTags.PARCELS, "Parcels");
        translationBuilder.add(MailTags.CANNOT_FIT_IN_PARCELS, "Cannot Fit In Parcel");

        translationBuilder.add("letter.editTitle", "Enter Letter Title");
        translationBuilder.add("letter.sign.title", "Title");
        translationBuilder.add("letter.finalizeWarning", "Note! When you sign the letter, it will no longer be editable.");

        translationBuilder.add("hud.spamton.recipient", "To: ");
        translationBuilder.add("hud.spamton.sender", "From: ");
        translationBuilder.add("hud.spamton.unknown", "Unknown");
    }
}
