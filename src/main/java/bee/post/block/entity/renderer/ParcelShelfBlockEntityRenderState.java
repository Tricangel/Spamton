package bee.post.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ParcelShelfBlockEntityRenderState extends BlockEntityRenderState {
    NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    Direction rotation;
    int lightCoordsInFront = 0;
}
