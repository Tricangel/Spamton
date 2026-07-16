package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.entity.StampType;
import bee.post.item.PlayerStampItem;
import bee.post.item.StampItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class MailItems {


    public static final Item STAMP = register("stamp", StampItem::new, new Item.Properties().component(MailItemComponents.STAMP_TYPE, StampType.SUN));

    public static final Item PLAYER_STAMP = register("player_stamp", PlayerStampItem::new, new Item.Properties());


    public static final Item TAPE = register("tape", Item::new, new Item.Properties().durability(128));
    public static final Item BOX_CUTTER = register("box_cutter", Item::new, new Item.Properties().durability(128));

    public static final Item CARDBOARD = register("cardboard", Item::new, new Item.Properties());


    public static void init() {
    }

    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Spamton.id(name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

}


