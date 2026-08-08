package bee.post.item.stamp;

import bee.post.registry.MailItems;
import bee.post.registry.MailTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class StampRecipe extends CustomRecipe {
    public static final StampRecipe INSTANCE = new StampRecipe();
    public static final MapCodec<StampRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, StampRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final RecipeSerializer<StampRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    @Override
    public boolean matches(CraftingInput recipeInput, Level level) {
        int paperCount = 0;
        boolean hasStamp = false;
        for (ItemStack stack : recipeInput.items()) {
            if (stack.is(Items.PAPER)) paperCount++;

            if (stack.is(MailTags.STAMP)) hasStamp = true;

            if (stack.is(Items.PLAYER_HEAD)) hasStamp = true;
        }
        return paperCount == 8 && hasStamp;
    }

    @Override
    public @NonNull ItemStack assemble(CraftingInput recipeInput) {
        ItemStack stamp = MailItems.STAMP.getDefaultInstance();
        for (ItemStack stack : recipeInput.items()) {
            if (stack.is(MailTags.STAMP)) stamp = stack.copyWithCount(2);
            if (stack.is(Items.PLAYER_HEAD)) {
                stamp.set(DataComponents.PROFILE, stack.get(DataComponents.PROFILE));
                stamp = stamp.copyWithCount(1);
            };
        }
        return stamp;
    }


    public static class StampRecipeType implements RecipeType<StampRecipe> {
        public static final StampRecipeType INSTANCE = new StampRecipeType();
    }


    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }
}
