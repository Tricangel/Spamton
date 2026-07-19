package bee.post.block.entity.renderer;

import bee.post.block.entity.ParcelShelfBlockEntity;
import bee.post.client.renderer.ParcelShelfSpecialRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class ParcelShelfBlockEntityRenderer implements BlockEntityRenderer<ParcelShelfBlockEntity, ParcelShelfBlockEntityRenderState> {
    BlockEntityRendererProvider.Context context;
    public ParcelShelfBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public ParcelShelfBlockEntityRenderState createRenderState() {
        return new ParcelShelfBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(ParcelShelfBlockEntity blockEntity, ParcelShelfBlockEntityRenderState renderState, float f, Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, f, vec3, crumblingOverlay);

        renderState.stacks = blockEntity.getItems();
        renderState.rotation = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);


    }

    @Override
    public void submit(ParcelShelfBlockEntityRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {


        for (int i = 0; i < blockEntityRenderState.stacks.size(); i++) {
            ItemStack stack = blockEntityRenderState.stacks.get(i);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                ParcelShelfSpecialRenderer shelfSpecialRenderer = new ParcelShelfSpecialRenderer.Unbaked().bake(context);
                poseStack.translate(0.5f, 1.5f, 0.5f);
                poseStack.mulPose(blockEntityRenderState.rotation.getRotation());
                poseStack.mulPose(Axis.XP.rotation(1.57f));
                poseStack.translate(0, 0, -0.01);


                poseStack.translate(getTranslation(i + 1));

                shelfSpecialRenderer.submit(shelfSpecialRenderer.extractArgument(stack), ItemDisplayContext.ON_SHELF, poseStack, submitNodeCollector, blockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, false, 0);
                poseStack.popPose();
            }
        }

    }

    private Vec3 getTranslation(int slot) {


        float x = 0;
        float y = 0;

        switch (slot) {
            case 1, 4, 7 -> x = 0;
            case 2, 5, 8 -> x = .312f;
            case 3, 6, 9 -> x = .624f;
        }

        switch (slot) {
            case 1, 2, 3 -> y = 0;
            case 4, 5, 6 -> y = .312f;
            case 7, 8, 9 -> y = .624f;
        }


        return new Vec3(x, y, 0);
    }

}
