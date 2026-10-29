package bee.post.registry;

import bee.post.Spamton;
import bee.post.entity.PackageEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MailEntityTypes {

    public static final  ResourceKey<EntityType<?>> PACKAGE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, Spamton.id("package"));


    public static final EntityType<PackageEntity> PACKAGE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            PACKAGE_KEY,
            EntityType.Builder.<PackageEntity>of(PackageEntity::new, MobCategory.MISC).sized(1, 1).build(PACKAGE_KEY)
    );

    public static void init() {}

}
