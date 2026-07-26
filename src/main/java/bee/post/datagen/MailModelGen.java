package bee.post.datagen;

import bee.post.registry.MailBlocks;
import bee.post.registry.MailItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class MailModelGen extends FabricModelProvider {
    public MailModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createNonTemplateHorizontalBlock(MailBlocks.PARCEL_SHELF);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {

        itemModelGenerators.generateFlatItem(MailItems.BOX_CUTTER, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.TAPE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.CARDBOARD, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.LETTER, ModelTemplates.FLAT_ITEM);

        itemModelGenerators.generateFlatItem(MailItems.WHITE_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.ORANGE_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.MAGENTA_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.LIGHT_BLUE_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.YELLOW_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.LIME_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.PINK_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.GRAY_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.LIGHT_GRAY_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.CYAN_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.PURPLE_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.BLUE_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.BROWN_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.GREEN_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.RED_PARCEL, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(MailItems.BLACK_PARCEL, ModelTemplates.FLAT_ITEM);

    }


}
