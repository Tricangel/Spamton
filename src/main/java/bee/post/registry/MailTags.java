package bee.post.registry;

import bee.post.Spamton;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MailTags {

    public static void init() {}

    public static final TagKey<Item> STAMP = TagKey.create(Registries.ITEM, Spamton.id("stamp"));

    public static final TagKey<Item> TAPE = TagKey.create(Registries.ITEM, Spamton.id("tape"));

    public static final TagKey<Item> BOX_CUTTER = TagKey.create(Registries.ITEM, Spamton.id("box_cutter"));

    public static final TagKey<Item> SAFE_BOX_CUTTER = TagKey.create(Registries.ITEM, Spamton.id("safe_box_cutter"));

}
