package bee.post.client.model;

import bee.post.Spamton;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class PlayerStampModel extends Model<PlayerStampModel.State>  {
    public static final ModelLayerLocation STAMP = new ModelLayerLocation(Spamton.id("stamp"), "main");
    public static final ModelLayerLocation STAMP_ITEM = new ModelLayerLocation(Spamton.id("stamp_item"), "main");
    public PlayerStampModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityTranslucent);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(39, 7).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(7, 7).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -16.0F, 5.0F, 1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createItemLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(79, 15).addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(15, 15).addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -24.0F, 8.0F, 0.0F, 3.1416F, -3.1416F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public static class State {

    }

}
