package bee.post.registry;

import bee.post.Spamton;
import bee.post.block.PackageBlock;
import bee.post.block.entity.StampType;
import bee.post.item.ParcelContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;

public class MailItemComponents {

    public static void init() {
    }



    public static final DataComponentType<StampType> STAMP_TYPE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Spamton.id("stamp_type"),
            DataComponentType.<StampType>builder().persistent(StampType.CODEC).build()
    );

    public static final DataComponentType<List<PackageBlock.Stamp>> STAMPS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Spamton.id("stamps"),
            DataComponentType.<List<PackageBlock.Stamp>>builder().persistent(PackageBlock.Stamp.CODEC.listOf()).build()
    );

    public static final DataComponentType<Boolean> DAMAGED = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Spamton.id("damaged"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static final DataComponentType<Boolean> TAPED = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Spamton.id("taped"),
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static final DataComponentType<ParcelContents> PARCEL_CONTENTS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Spamton.id("parcel_contents"),
            DataComponentType.<ParcelContents>builder().persistent(ParcelContents.CODEC).build()
    );

}
