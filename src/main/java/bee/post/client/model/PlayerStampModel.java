package bee.post.client.model;

import bee.post.Spamton;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PlayerStampModel extends PlayerStampModelBase{
    public static final ModelLayerLocation STAMP = new ModelLayerLocation(Spamton.id("stamp"), "main");
    public PlayerStampModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(39, 7).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.1F))
                .texOffs(7, 7).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -16.0F, 5.0F, 1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

}
