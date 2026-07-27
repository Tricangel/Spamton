package bee.post.datagen;

import bee.post.registry.MailBlocks;
import bee.post.registry.MailItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class MailModelGen extends FabricModelProvider {
    public MailModelGen(FabricPackOutput output) {
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


        MailItems.PARCEL.forEach(item -> {
            itemModelGenerators.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        });
    }


}
