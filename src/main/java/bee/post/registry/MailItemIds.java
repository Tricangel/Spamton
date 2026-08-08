package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.Stamp;
import bee.post.item.LetterItem;
import bee.post.item.PackageBlockItem;
import bee.post.item.component.ParcelContents;
import bee.post.item.component.PostalInfoComponent;
import bee.post.item.stamp.StampItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.ColorCollection;

public class MailItemIds {

    public static final ResourceKey<Item> STAMP = register("stamp");

    public static final ResourceKey<Item> TAPE = register("tape");
    public static final ResourceKey<Item> BOX_CUTTER = register("box_cutter");

    public static final ResourceKey<Item> CARDBOARD = register("cardboard");

    public static final ResourceKey<Item> LETTER = register("letter");


    public static final ResourceKey<Item> PACKAGE = register("package");

    public static final ColorCollection<ResourceKey<Item>> PARCEL = ColorCollection.prefixWithColor(ColorCollection.create("parcel")).map(MailItemIds::register);


    private static ResourceKey<Item> register(String name) {
        return ResourceKey.create(Registries.ITEM, Spamton.id(name));
    }

}
