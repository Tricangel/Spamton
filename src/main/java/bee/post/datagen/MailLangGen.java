package bee.post.datagen;

import bee.post.registry.MailBlocks;
import bee.post.registry.MailItems;
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
        translationBuilder.add("tag.item.spamton.stamp", "Stamps");
        translationBuilder.add("tag.item.spamton.tape", "Tape");
        translationBuilder.add("tag.item.spamton.box_cutter", "Box Cutter");
        translationBuilder.add("hud.spamton.sender", "Sender");
        translationBuilder.add("hud.spamton.receiver", "Recipient");

    }
}
