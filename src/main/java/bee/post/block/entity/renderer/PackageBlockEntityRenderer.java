package bee.post.block.entity.renderer;

import bee.post.block.PackageBlock;
import bee.post.block.entity.PackageBlockEntity;
import bee.post.client.model.PlayerStampModel;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PackageBlockEntityRenderer implements BlockEntityRenderer<PackageBlockEntity, PackageBlockEntityRenderState> {
    private final ItemModelResolver itemRenderer;
    private final BlockEntityRendererProvider.Context context;

    public PackageBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.itemModelResolver();
        this.context = context;
    }


    @Override
    public PackageBlockEntityRenderState createRenderState() {
        return new PackageBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(PackageBlockEntity blockEntity, PackageBlockEntityRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);

        blockEntityRenderState.stamps = blockEntity.getStamps();
        blockEntityRenderState.boxDirection = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

    }

    @Override
    public void submit(PackageBlockEntityRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {


        for (PackageBlock.Stamp stamp : blockEntityRenderState.stamps) {
            poseStack.pushPose();
            poseStack.translate(.5, .5, .5);
            poseStack.translate(stamp.pos());


            poseStack.mulPose(stamp.direction().getRotation());


            if (stamp.profile().isEmpty()) {
                poseStack.scale(.25f, .25f, .25f);
                if (stamp.direction().equals(Direction.WEST) || stamp.direction().equals(Direction.EAST)) {
                    poseStack.mulPose(Direction.EAST.getRotation());
                } else poseStack.mulPose(Direction.NORTH.getRotation());

                poseStack.mulPose(Axis.ZP.rotation((float) (stamp.rotation())));
                ItemStackRenderState state = new ItemStackRenderState();
                ItemStack stack = MailItems.STAMP.getDefaultInstance();
                stack.set(MailItemComponents.STAMP_TYPE, stamp.stampType());
                itemRenderer.updateForTopItem(state, stack, ItemDisplayContext.GUI, null, null, 0);

                state.submit(poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, 0);
            } else {
                poseStack.scale(.5f, .5f, .5f);

                poseStack.translate(0, -.5f, 0f);
                poseStack.mulPose(Axis.YP.rotation((float) (stamp.rotation())));
                PlayerSkinRenderCache cache = new PlayerSkinRenderCache(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getSkinManager(), Minecraft.getInstance().services().profileResolver());
                PlayerSkinRenderCache.RenderInfo renderInfo = cache.getOrDefault(stamp.profile().get());
                PlayerStampModel stampModel = new PlayerStampModel(context.entityModelSet().bakeLayer(PlayerStampModel.STAMP));

                submitNodeCollector.submitModel(stampModel, new PlayerStampModel.State(), poseStack, renderInfo.renderType(), 15728880, OverlayTexture.NO_OVERLAY, 0, null);


            }


            poseStack.popPose();
        }

    }
}
