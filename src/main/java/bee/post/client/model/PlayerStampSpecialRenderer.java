package bee.post.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class PlayerStampSpecialRenderer implements SpecialModelRenderer<PlayerSkinRenderCache.RenderInfo> {
    private final PlayerSkinRenderCache playerSkinRenderCache;
    private final PlayerStampModelBase modelBase;
    private PlayerStampSpecialRenderer(final PlayerSkinRenderCache playerSkinRenderCache, final PlayerStampModelBase modelBase) {
        this.playerSkinRenderCache = playerSkinRenderCache;
        this.modelBase = modelBase;
    }

    @Override
    public void submit(PlayerSkinRenderCache.@Nullable RenderInfo argument, @NonNull ItemDisplayContext itemDisplayContext, @NonNull PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, boolean bl, int k) {
        RenderType renderType = argument != null ? argument.renderType() : PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
        submitSkull(poseStack, submitNodeCollector, i, this.modelBase, renderType, k, null);
    }

    public static void submitSkull(
            final PoseStack poseStack,
            final SubmitNodeCollector submitNodeCollector,
            final int lightCoords,
            final PlayerStampModelBase model,
            final RenderType renderType,
            final int outlineColor,
            final ModelFeatureRenderer.CrumblingOverlay breakProgress
    ) {
        PlayerStampModelBase.State modelState = new PlayerStampModelBase.State();
        submitNodeCollector.submitModel(model, modelState, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, breakProgress);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.modelBase.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public PlayerSkinRenderCache.RenderInfo extractArgument(final ItemStack stack) {
        ResolvableProfile profile = stack.get(DataComponents.PROFILE);
        return profile == null ? null : this.playerSkinRenderCache.getOrDefault(profile);
    }

    public record Unbaked() implements net.minecraft.client.renderer.special.SpecialModelRenderer.Unbaked {
        public static final MapCodec<PlayerStampSpecialRenderer.Unbaked> MAP_CODEC = MapCodec.unit(PlayerStampSpecialRenderer.Unbaked::new);

        @Override
        public @Nullable SpecialModelRenderer<?> bake(BakingContext bakingContext) {
            return null;
        }

        @Override
        public MapCodec<PlayerStampSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        public PlayerStampSpecialRenderer bake(final BlockEntityRendererProvider.Context  context) {
            PlayerStampModelBase model = new PlayerStampModel(context.entityModelSet().bakeLayer(PlayerStampModel.STAMP));
            return new PlayerStampSpecialRenderer(context.playerSkinRenderCache(), model);
        }

    }
}
