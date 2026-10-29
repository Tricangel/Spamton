package bee.post.entity.renderer;

import bee.post.block.Stamp;
import bee.post.client.model.PlayerStampModel;
import bee.post.entity.PackageEntity;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PackageEntityRenderer extends EntityRenderer<PackageEntity, PackageEntityRenderState> {
    private final ItemModelResolver itemRenderer;
    private final EntityRendererProvider.Context context;

    public PackageEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
        this.itemRenderer = context.getItemModelResolver();
    }

    @Override
    public PackageEntityRenderState createRenderState() {
        return new PackageEntityRenderState();
    }

    @Override
    public void extractRenderState(PackageEntity entity, PackageEntityRenderState entityRenderState, float f) {
        super.extractRenderState(entity, entityRenderState, f);
        entityRenderState.stamps = entity.stamps;
        entityRenderState.state = entity.state;
    }

    @Override
    public void submit(PackageEntityRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {

        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);
        submitNodeCollector.submitBlock(poseStack, entityRenderState.state, entityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, entityRenderState.outlineColor);
        poseStack.popPose();
        for (Stamp stamp : entityRenderState.stamps) {
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
                PlayerStampModel stampModel = new PlayerStampModel(context.getModelSet().bakeLayer(PlayerStampModel.STAMP));

                submitNodeCollector.submitModel(stampModel, new PlayerStampModel.State(), poseStack, renderInfo.renderType(), 15728880, OverlayTexture.NO_OVERLAY, 0, null);


            }


            poseStack.popPose();
        }
    }
}
