package bee.post;

import bee.post.block.entity.renderer.PackageBlockEntityRenderer;
import bee.post.block.entity.renderer.ParcelShelfBlockEntityRenderer;
import bee.post.client.overlay.PeekParcelShelfOverlay;
import bee.post.client.renderer.PlayerStampSpecialRenderer;
import bee.post.item.ClientParcelContentsTooltip;
import bee.post.item.ParcelContents;
import bee.post.registry.MailBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class SpamtonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(MailBlockEntities.PACKAGE_BLOCK_ENTITY, PackageBlockEntityRenderer::new);

        BlockEntityRenderers.register(MailBlockEntities.PARCEL_SHELF_BLOCK_ENTITY, ParcelShelfBlockEntityRenderer::new);

        HudElementRegistry.addFirst(Spamton.id("peek_parcel_shelf"), new PeekParcelShelfOverlay());

        SpecialModelRenderers.ID_MAPPER.put(Spamton.id("player_stamp"), PlayerStampSpecialRenderer.Unbaked.MAP_CODEC);

        TooltipComponentCallback.EVENT.register(component -> {
            if (component instanceof ParcelContents contents) {
                return new ClientParcelContentsTooltip(contents);
            }
            return null;
        });
    }
}
