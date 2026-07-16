package bee.post.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;


public class PlayerStampModelBase extends Model<PlayerStampModelBase.State> {
    public PlayerStampModelBase(ModelPart modelPart) {
            super(modelPart, RenderTypes::entityTranslucent);
    }

    public static class State {

    }

}
