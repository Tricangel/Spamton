package bee.post.datagen;

import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class MailTagGen extends FabricTagProvider.ItemTagProvider {
    public MailTagGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(MailTags.STAMP)
                .add(MailItems.PLAYER_STAMP)
                .add(MailItems.STAMP);

        valueLookupBuilder(MailTags.TAPE)
                .add(Items.HONEYCOMB)
                .add(Items.HONEY_BOTTLE)
                .add(MailItems.TAPE);

        valueLookupBuilder(MailTags.SAFE_BOX_CUTTER)
                .add(MailItems.BOX_CUTTER);

        valueLookupBuilder(MailTags.BOX_CUTTER)
                .forceAddTag(MailTags.SAFE_BOX_CUTTER)
                .forceAddTag(ItemTags.SWORDS)
                .forceAddTag(ItemTags.SHARP_WEAPON_ENCHANTABLE);
    }
}
