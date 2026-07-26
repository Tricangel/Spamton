package bee.post.client.renderer;

import bee.post.client.model.PlayerStampModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class PlayerStampSpecialRenderer implements SpecialModelRenderer<PlayerSkinRenderCache.RenderInfo> {
    private final PlayerSkinRenderCache playerSkinRenderCache;
    private final PlayerStampModel modelBase;
    private PlayerStampSpecialRenderer(final PlayerSkinRenderCache playerSkinRenderCache, final PlayerStampModel modelBase) {
        this.playerSkinRenderCache = playerSkinRenderCache;
        this.modelBase = modelBase;
    }

    @Override
    public void submit(PlayerSkinRenderCache.@Nullable RenderInfo argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        RenderType renderType = argument != null ? argument.renderType() : PlayerSkinRenderCache.DEFAULT_PLAYER_SKIN_RENDER_TYPE;
        submitNodeCollector.submitModel(this.modelBase, new PlayerStampModel.State(), poseStack, renderType, lightCoords, overlayCoords, outlineColor, null);

    }

    @Override
    public void getExtents(@NonNull Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.modelBase.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public PlayerSkinRenderCache.RenderInfo extractArgument(final ItemStack stack) {
        ResolvableProfile profile = stack.get(DataComponents.PROFILE);
        return profile == null ? null : this.playerSkinRenderCache.getOrDefault(profile);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<PlayerSkinRenderCache.RenderInfo> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(PlayerStampSpecialRenderer.Unbaked::new);

        @Override
        public @NonNull PlayerStampSpecialRenderer bake(BakingContext bakingContext) {
            PlayerStampModel model = new PlayerStampModel(bakingContext.entityModelSet().bakeLayer(PlayerStampModel.STAMP_ITEM));
            return new PlayerStampSpecialRenderer(bakingContext.playerSkinRenderCache(), model);
        }

        @Override
        public @NonNull MapCodec<PlayerStampSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }


    }
}
