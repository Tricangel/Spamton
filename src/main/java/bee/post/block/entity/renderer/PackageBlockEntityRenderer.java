package bee.post.block.entity.renderer;

import bee.post.block.PackageBlock;
import bee.post.block.entity.PackageBlockEntity;
import bee.post.client.model.PlayerStampSpecialRenderer;
import bee.post.registry.MailItemComponents;
import bee.post.registry.MailItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.PlayerHeadSpecialRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.players.ProfileResolver;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

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

        blockEntity.getStamps().forEach(stamp -> {
            stamp.profile().ifPresent(resolvableProfile ->  {

                PlayerStampSpecialRenderer.Unbaked unbaked = new PlayerStampSpecialRenderer.Unbaked();

                blockEntityRenderState.renderers.add(
                        unbaked.bake(context)
                );

            });

        });

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




                for (PlayerStampSpecialRenderer renderer : blockEntityRenderState.renderers) {
                    renderer.submit(renderInfo, ItemDisplayContext.GUI, poseStack, submitNodeCollector, 15728880, OverlayTexture.NO_OVERLAY, true, 0);
                }

            }


            poseStack.popPose();
        }

    }
}
