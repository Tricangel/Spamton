package bee.post.datagen;

import bee.post.Spamton;
import bee.post.item.ParcelItem;
import bee.post.registry.MailBlocks;
import bee.post.registry.MailItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MailRecipeGen extends FabricRecipeProvider {
    public MailRecipeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            final HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

            @Override
            public void buildRecipes() {

                shaped(RecipeCategory.BUILDING_BLOCKS, MailBlocks.PACKAGE.asItem())
                        .pattern("xx")
                        .pattern("xx")
                        .define('x', MailItems.CARDBOARD)
                        .unlockedBy(getHasName(MailBlocks.PACKAGE.asItem()), has(MailItems.CARDBOARD))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, MailItems.CARDBOARD)
                        .pattern("xx")
                        .pattern("xx")
                        .define('x', Items.PAPER)
                        .unlockedBy(getHasName(MailItems.CARDBOARD), has(Items.PAPER))
                        .save(output);

                shaped(RecipeCategory.TOOLS, MailItems.TAPE)
                        .pattern("xx")
                        .pattern("y ")
                        .define('x', Items.PAPER)
                        .define('y', Items.HONEY_BOTTLE)
                        .unlockedBy(getHasName(MailItems.TAPE), has(Items.PAPER))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, MailBlocks.PARCEL_SHELF.asItem())
                        .pattern("xxx")
                        .pattern("yyy")
                        .pattern("xxx")
                        .define('x', ItemTags.PLANKS)
                        .define('y', ItemTags.WOODEN_SLABS)
                        .unlockedBy(getHasName(MailBlocks.PARCEL_SHELF), has(ItemTags.PLANKS))
                        .save(output);

                shapeless(RecipeCategory.TOOLS, MailItems.BOX_CUTTER)
                        .requires(Items.IRON_INGOT)
                        .requires(Items.RED_DYE)
                        .unlockedBy(getHasName(MailItems.BOX_CUTTER), has(Items.IRON_INGOT))
                        .save(output);

                shapeless(RecipeCategory.TOOLS, MailItems.LETTER)
                        .requires(Items.PAPER)
                        .requires(Items.PAPER)
                        .requires(MailItems.CARDBOARD)
                        .unlockedBy(getHasName(MailItems.LETTER), has(Items.PAPER))
                        .save(output);


                List<Item> dyes = List.of(
                        Items.WHITE_DYE,
                        Items.ORANGE_DYE,
                        Items.MAGENTA_DYE,
                        Items.LIGHT_BLUE_DYE,
                        Items.YELLOW_DYE,
                        Items.LIME_DYE,
                        Items.PINK_DYE,
                        Items.GRAY_DYE,
                        Items.LIGHT_GRAY_DYE,
                        Items.CYAN_DYE,
                        Items.PURPLE_DYE,
                        Items.BLUE_DYE,
                        Items.BROWN_DYE,
                        Items.GREEN_DYE,
                        Items.RED_DYE,
                        Items.BLACK_DYE);

                colorItemWithDye(dyes, MailItems.PARCELS, "parcel", RecipeCategory.TOOLS);
                parcels(MailItems.PARCELS);


            }


            public void parcels(List<Item> items) {
                for (Item item : items) {
                    if (!(item instanceof ParcelItem parcelItem)) continue;
                    Item color = DyeItem.byColor(parcelItem.color);

                    shapeless(RecipeCategory.TOOLS, parcelItem)
                            .requires(MailItems.CARDBOARD)
                            .requires(Items.PAPER)
                            .requires(MailItems.CARDBOARD)
                            .requires(color)
                            .unlockedBy(getHasName(parcelItem), has(Items.PAPER))
                            .save(output);

                }
            }

        };
    }

    @Override
    public @NonNull String getName() {
        return "Recipe Gen";
    }
}
