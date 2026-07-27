package bee.post.client.renderer;

import bee.post.Spamton;
import bee.post.client.model.ParcelModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemStack;
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
    public void submit(@Nullable RenderType argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.submitModel(this.modelBase, new ParcelModel.State(), poseStack, argument, lightCoords, overlayCoords, outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        this.modelBase.root().getExtentsForGui(poseStack, consumer);
    }

    @Override
    public @Nullable RenderType extractArgument(ItemStack itemStack) {

        String name = itemStack.getItemName().getString();

        name = name.replace(" ", "_");
        name = name.toLowerCase();


        return RenderTypes.armorTranslucent(Spamton.id("textures/shelf/" + name + ".png"));
    }


    public record Unbaked() implements SpecialModelRenderer.Unbaked<RenderType> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(ParcelShelfSpecialRenderer.Unbaked::new);

        @Override
        public @NonNull SpecialModelRenderer<RenderType> bake(BakingContext bakingContext) {
            ParcelModel model = new ParcelModel(bakingContext.entityModelSet().bakeLayer(ParcelModel.PARCEL));
            return new ParcelShelfSpecialRenderer(model);
        }

        public @NonNull ParcelShelfSpecialRenderer bake(BlockEntityRendererProvider.Context bakingContext) {
            ParcelModel model = new ParcelModel(bakingContext.entityModelSet().bakeLayer(ParcelModel.PARCEL));
            return new ParcelShelfSpecialRenderer(model);
        }

        @Override
        public @NonNull MapCodec<ParcelShelfSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }


    }
}
