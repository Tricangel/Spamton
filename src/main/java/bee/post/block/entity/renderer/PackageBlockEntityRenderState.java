package bee.post.block.entity.renderer;

import bee.post.block.Stamp;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

public class PackageBlockEntityRenderState extends BlockEntityRenderState {
    public List<Stamp> stamps = new ArrayList<>();
    public Direction boxDirection = Direction.EAST;
}
