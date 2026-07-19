package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.entity.StampType;
import bee.post.item.LetterItem;
import bee.post.item.ParcelItem;
import bee.post.item.StampItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class MailItems {


    public static final Item STAMP = register("stamp", StampItem::new, new Item.Properties().component(MailItemComponents.STAMP_TYPE, StampType.SUN));

    public static final Item TAPE = register("tape", Item::new, new Item.Properties().durability(128));
    public static final Item BOX_CUTTER = register("box_cutter", Item::new, new Item.Properties().durability(128));

    public static final Item CARDBOARD = register("cardboard", Item::new, new Item.Properties());

    public static final Item BLUE_LETTER = register("blue_letter", properties -> new LetterItem(properties, DyeColor.BLUE), new Item.Properties());

    public static final Item BLUE_PARCEL = register("blue_parcel", properties -> new ParcelItem(properties, DyeColor.BLUE), new Item.Properties());


    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Spamton.id(name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}


