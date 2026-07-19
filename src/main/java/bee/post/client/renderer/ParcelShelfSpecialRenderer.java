package bee.post.client.renderer;

import bee.post.Spamton;
import bee.post.client.model.ParcelModel;
import bee.post.client.model.PlayerStampModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
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

public class ParcelShelfSpecialRenderer implements SpecialModelRenderer<RenderType> {

    private final ParcelModel modelBase;
    private ParcelShelfSpecialRenderer(final ParcelModel modelBase) {

        this.modelBase = modelBase;
    }

    @Override
    public void submit(RenderType argument, @NonNull ItemDisplayContext itemDisplayContext, @NonNull PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, boolean bl, int k) {
        submitNodeCollector.submitModel(this.modelBase, new ParcelModel.State(), poseStack, argument, i, OverlayTexture.NO_OVERLAY, k, null);
    }


    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.modelBase.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public @Nullable RenderType extractArgument(ItemStack itemStack) {

        return RenderTypes.armorTranslucent(Spamton.id("textures/block/red_parcel.png"));
    }


    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(ParcelShelfSpecialRenderer.Unbaked::new);

        @Override
        public @NonNull SpecialModelRenderer<?> bake(BakingContext bakingContext) {
            ParcelModel model = new ParcelModel(bakingContext.entityModelSet().bakeLayer(ParcelModel.PARCEL));
            return new ParcelShelfSpecialRenderer(model);
        }

        public @NonNull ParcelShelfSpecialRenderer bake(BlockEntityRendererProvider.Context bakingContext) {
            ParcelModel model = new ParcelModel(bakingContext.entityModelSet().bakeLayer(ParcelModel.PARCEL));
            return new ParcelShelfSpecialRenderer(model);
        }

        @Override
        public MapCodec<ParcelShelfSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }


    }
}
