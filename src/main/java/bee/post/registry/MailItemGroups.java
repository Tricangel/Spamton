package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.Stamp;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MailItemGroups {

    public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(), Spamton.id("creative_tab"));


    public static final CreativeModeTab TAB = FabricCreativeModeTab.builder()
            .icon(MailItems.GREEN_PARCEL::getDefaultInstance)
            .title(Component.translatable("itemgroup.spamton"))
            .displayItems(((itemDisplayParameters, output) -> {
                output.accept(new ItemStack(MailItems.CARDBOARD));
                output.accept(new ItemStack(MailBlocks.PACKAGE));
                for (Stamp.StampType type : Stamp.StampType.values()) {
                    ItemStack stack = new ItemStack(MailItems.STAMP);
                    stack.set(MailItemComponents.STAMP_TYPE, type);
                    output.accept(stack);
                }
                output.accept(new ItemStack(MailItems.BOX_CUTTER));
                output.accept(new ItemStack(MailItems.TAPE));
                output.accept(new ItemStack(MailBlocks.PARCEL_SHELF));
                output.accept(new ItemStack(MailItems.LETTER));

                for (Item item : MailItems.PARCELS) {
                    output.accept(item.getDefaultInstance());
                }

            })).build();

    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, TAB);
    }

}
