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

        translationBuilder.add(MailItems.STAMP, "Stamp");
        translationBuilder.add(MailItems.BOX_CUTTER, "Box Cutter");
        translationBuilder.add(MailItems.TAPE, "Tape");
        translationBuilder.add(MailBlocks.PACKAGE, "Package");
        translationBuilder.add("stamp.spamton.moon", "Moon Stamp");
        translationBuilder.add("stamp.spamton.sun", "Sun Stamp");
        translationBuilder.add("stamp.spamton.pickaxe", "Pickaxe Stamp");
        translationBuilder.add(MailTags.STAMP, "Stamps");
        translationBuilder.add(MailTags.TAPE, "Tape");
        translationBuilder.add(MailTags.BOX_CUTTER, "Box Cutters");
        translationBuilder.add(MailTags.SAFE_BOX_CUTTER, "Safe Box Cutters");

        translationBuilder.add("letter.editTitle", "Enter Letter Title");
        translationBuilder.add("letter.editAuthor", "Enter Letter Author");
        translationBuilder.add("letter.sign.title", "Title");
        translationBuilder.add("letter.sign.author", "Author");

        translationBuilder.add("letter.finalizeWarning", "Note! When you sign the letter, it will no longer be editable.");

    }
}
