package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.Stamp;
import bee.post.item.LetterItem;
import bee.post.item.PackageBlockItem;
import bee.post.item.ParcelItem;
import bee.post.item.component.ParcelContents;
import bee.post.item.component.PostalInfoComponent;
import bee.post.item.stamp.StampItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.ColorCollection;

import java.util.List;
import java.util.function.Function;

public class MailItems {

    public static final Item STAMP = register("stamp", StampItem::new, new Item.Properties().component(MailItemComponents.STAMP_TYPE, Stamp.StampType.SUN));

    public static final Item TAPE = register("tape", Item::new, new Item.Properties());
    public static final Item BOX_CUTTER = register("box_cutter", Item::new, new Item.Properties().sword(ToolMaterial.COPPER, 2.0F, -2.4F));

    public static final Item CARDBOARD = register("cardboard", Item::new, new Item.Properties());

    public static final Item LETTER = register("letter", LetterItem::new, new Item.Properties().stacksTo(1));


    public static final Item PACKAGE = register("package", properties -> new PackageBlockItem(MailBlocks.PACKAGE, properties), new Item.Properties().stacksTo(16).component(MailItemComponents.POSTAL_INFO, PostalInfoComponent.EMPTY));

    public static final ColorCollection<Item> PARCEL = ColorCollection.registerItems(MailItemIds.PARCEL, (name, dyeColor) -> register(name, properties -> new ParcelItem(properties, dyeColor), new Item.Properties().component(MailItemComponents.PARCEL_CONTENTS, ParcelContents.EMPTY).component(MailItemComponents.POSTAL_INFO, PostalInfoComponent.EMPTY)));

    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Spamton.id(name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static <GenericItem extends Item> GenericItem register(ResourceKey<Item> key, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        GenericItem item = itemFactory.apply(settings.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }

}


