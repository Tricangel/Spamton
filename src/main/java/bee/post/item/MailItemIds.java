package bee.post.item;

import bee.post.Spamton;
import bee.post.block.Stamp;
import bee.post.registry.MailItemComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.ColorCollection;

import java.util.Optional;

public class MailItemIds {

    public static final ResourceKey<Item> STAMP = create("stamp");

    public static final ResourceKey<Item> TAPE = create("tape");
    public static final ResourceKey<Item> BOX_CUTTER = create("box_cutter");

    public static final ResourceKey<Item> CARDBOARD = create("cardboard");

    public static final ResourceKey<Item> LETTER = create("letter");


    public static final ColorCollection<ResourceKey<Item>> PARCELS = ColorCollection.prefixWithColor(ColorCollection.create("parcels")).map(MailItemIds::create);




    private static ResourceKey<Item> create(String string) {
        return ResourceKey.create(Registries.ITEM, Spamton.id(string));
    }
}
