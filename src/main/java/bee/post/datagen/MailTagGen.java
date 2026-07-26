package bee.post.datagen;

import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MailTagGen extends FabricTagsProvider.ItemTagsProvider {
    public MailTagGen(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        valueLookupBuilder(MailTags.STAMP)
                .add(MailItems.STAMP);

        valueLookupBuilder(MailTags.LETTERS)
                .add(Items.FILLED_MAP)
                .add(Items.WRITTEN_BOOK)
                .add(Items.WRITABLE_BOOK)
                .add(MailItems.LETTER);

        valueLookupBuilder(MailTags.PARCELS)
                .add(MailItems.WHITE_PARCEL)
                .add(MailItems.ORANGE_PARCEL)
                .add(MailItems.MAGENTA_PARCEL)
                .add(MailItems.LIGHT_BLUE_PARCEL)
                .add(MailItems.YELLOW_PARCEL)
                .add(MailItems.LIME_PARCEL)
                .add(MailItems.PINK_PARCEL)
                .add(MailItems.GRAY_PARCEL)
                .add(MailItems.LIGHT_GRAY_PARCEL)
                .add(MailItems.CYAN_PARCEL)
                .add(MailItems.PURPLE_PARCEL)
                .add(MailItems.BLUE_PARCEL)
                .add(MailItems.BROWN_PARCEL)
                .add(MailItems.GREEN_PARCEL)
                .add(MailItems.RED_PARCEL)
                .add(MailItems.BLACK_PARCEL);

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

        valueLookupBuilder(MailTags.CANNOT_FIT_IN_PARCELS)
                .forceAddTag(MailTags.PARCELS)
                .forceAddTag(ItemTags.BUNDLES)
                .forceAddTag(ItemTags.SHULKER_BOXES);

    }
}
