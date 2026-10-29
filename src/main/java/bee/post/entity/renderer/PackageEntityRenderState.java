package bee.post.entity.renderer;

import bee.post.block.Stamp;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class PackageEntityRenderState extends EntityRenderState {
    public List<Stamp> stamps = new ArrayList<>();
    public BlockState state;
}
