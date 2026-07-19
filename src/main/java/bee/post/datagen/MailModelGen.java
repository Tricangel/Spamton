package bee.post.datagen;

import bee.post.client.renderer.PlayerStampSpecialRenderer;
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


    }


}
