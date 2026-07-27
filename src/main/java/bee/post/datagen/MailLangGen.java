package bee.post.datagen;

import bee.post.registry.MailBlocks;
import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class MailLangGen extends FabricLanguageProvider {
    public MailLangGen(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
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

        translationBuilder.add(MailItems.PARCEL.white(), "White Parcel");
        translationBuilder.add(MailItems.PARCEL.orange(), "Orange Parcel");
        translationBuilder.add(MailItems.PARCEL.magenta(), "Magenta Parcel");
        translationBuilder.add(MailItems.PARCEL.lightBlue(), "Light Blue Parcel");
        translationBuilder.add(MailItems.PARCEL.yellow(), "Yellow Parcel");
        translationBuilder.add(MailItems.PARCEL.lime(), "Lime Parcel");
        translationBuilder.add(MailItems.PARCEL.pink(), "Pink Parcel");
        translationBuilder.add(MailItems.PARCEL.gray(), "Gray Parcel");
        translationBuilder.add(MailItems.PARCEL.lightGray(), "Light Gray Parcel");
        translationBuilder.add(MailItems.PARCEL.cyan(), "Cyan Parcel");
        translationBuilder.add(MailItems.PARCEL.purple(), "Purple Parcel");
        translationBuilder.add(MailItems.PARCEL.blue(), "Blue Parcel");
        translationBuilder.add(MailItems.PARCEL.brown(), "Brown Parcel");
        translationBuilder.add(MailItems.PARCEL.green(), "Green Parcel");
        translationBuilder.add(MailItems.PARCEL.red(), "Red Parcel");
        translationBuilder.add(MailItems.PARCEL.black(), "Black Parcel");


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
