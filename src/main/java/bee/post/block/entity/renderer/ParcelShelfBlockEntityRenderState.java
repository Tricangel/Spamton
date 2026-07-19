package bee.post.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ParcelShelfBlockEntityRenderState extends BlockEntityRenderState {
    NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
    Direction rotation;
}
