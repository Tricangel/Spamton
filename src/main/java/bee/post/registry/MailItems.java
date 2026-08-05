package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.Stamp;
import bee.post.item.LetterItem;
import bee.post.item.ParcelContents;
import bee.post.item.ParcelItem;
import bee.post.item.StampItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MailItems {


    public static final Item STAMP = register("stamp", StampItem::new, new Item.Properties().component(MailItemComponents.STAMP_TYPE, Stamp.StampType.SUN));

    public static final Item TAPE = register("tape", Item::new, new Item.Properties());
    public static final Item BOX_CUTTER = register("box_cutter", Item::new, new Item.Properties().sword(ToolMaterial.COPPER, 2.0F, -2.4F));

    public static final Item CARDBOARD = register("cardboard", Item::new, new Item.Properties());

    public static final Item LETTER = register("letter", LetterItem::new, new Item.Properties().stacksTo(1));

    private static final ParcelContents DEFAULT_CONTENTS = ParcelContents.EMPTY;
    
    public static final Item WHITE_PARCEL = register("white_parcel", properties -> new ParcelItem(properties, DyeColor.WHITE), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item ORANGE_PARCEL = register("orange_parcel", properties -> new ParcelItem(properties, DyeColor.ORANGE), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item MAGENTA_PARCEL = register("magenta_parcel", properties -> new ParcelItem(properties, DyeColor.MAGENTA), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item LIGHT_BLUE_PARCEL = register("light_blue_parcel", properties -> new ParcelItem(properties, DyeColor.LIGHT_BLUE ), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item YELLOW_PARCEL = register("yellow_parcel", properties -> new ParcelItem(properties, DyeColor.YELLOW), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item LIME_PARCEL = register("lime_parcel", properties -> new ParcelItem(properties, DyeColor.LIME), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item PINK_PARCEL = register("pink_parcel", properties -> new ParcelItem(properties, DyeColor.PINK), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item GRAY_PARCEL = register("gray_parcel", properties -> new ParcelItem(properties, DyeColor.GRAY), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item LIGHT_GRAY_PARCEL = register("light_gray_parcel", properties -> new ParcelItem(properties, DyeColor.LIGHT_GRAY), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item CYAN_PARCEL = register("cyan_parcel", properties -> new ParcelItem(properties, DyeColor.CYAN), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item PURPLE_PARCEL = register("purple_parcel", properties -> new ParcelItem(properties, DyeColor.PURPLE), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item BLUE_PARCEL = register("blue_parcel", properties -> new ParcelItem(properties, DyeColor.BLUE), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item BROWN_PARCEL = register("brown_parcel", properties -> new ParcelItem(properties, DyeColor.BROWN), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item GREEN_PARCEL = register("green_parcel", properties -> new ParcelItem(properties, DyeColor.GREEN), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item RED_PARCEL = register("red_parcel", properties -> new ParcelItem(properties, DyeColor.RED), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));
    public static final Item BLACK_PARCEL = register("black_parcel", properties -> new ParcelItem(properties, DyeColor.BLACK), new Item.Properties().stacksTo(1).component(MailItemComponents.PARCEL_CONTENTS, DEFAULT_CONTENTS));

    public static final List<Item> PARCELS = List.of(MailItems.WHITE_PARCEL, MailItems.ORANGE_PARCEL, MailItems.MAGENTA_PARCEL, MailItems.LIGHT_BLUE_PARCEL,
            MailItems.YELLOW_PARCEL, MailItems.LIME_PARCEL, MailItems.PINK_PARCEL, MailItems.GRAY_PARCEL, MailItems.LIGHT_GRAY_PARCEL,
            MailItems.CYAN_PARCEL, MailItems.PURPLE_PARCEL, MailItems.BLUE_PARCEL, MailItems.BROWN_PARCEL, MailItems.GREEN_PARCEL,
            MailItems.RED_PARCEL, MailItems.BLACK_PARCEL);

    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Spamton.id(name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}


