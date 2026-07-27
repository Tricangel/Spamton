package bee.post.datagen;

import bee.post.Spamton;
import bee.post.registry.MailItemIds;
import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
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
        builder(MailTags.STAMP)
                .add(MailItemIds.STAMP);

        builder(MailTags.LETTERS)
                .add(ItemIds.FILLED_MAP)
                .add(ItemIds.WRITTEN_BOOK)
                .add(ItemIds.WRITABLE_BOOK)
                .add(MailItemIds.STAMP);

        builder(MailTags.PARCELS)
                .addAll(MailItemIds.PARCEL);



        builder(MailTags.TAPE)
                .add(ItemIds.HONEYCOMB)
                .add(ItemIds.HONEY_BOTTLE)
                .add(MailItemIds.TAPE);

        builder(MailTags.SAFE_BOX_CUTTER)
                .add(MailItemIds.BOX_CUTTER);

        builder(MailTags.BOX_CUTTER)
                .forceAddTag(MailTags.SAFE_BOX_CUTTER)
                .forceAddTag(ItemTags.SWORDS)
                .forceAddTag(ItemTags.SHARP_WEAPON_ENCHANTABLE);

        builder(MailTags.CANNOT_FIT_IN_PARCELS)
                .forceAddTag(MailTags.PARCELS)
                .forceAddTag(ItemTags.BUNDLES)
                .forceAddTag(ItemTags.SHULKER_BOXES);

    }

}
