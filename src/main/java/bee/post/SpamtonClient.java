package bee.post;

import bee.post.block.entity.ParcelShelfBlockEntity;
import bee.post.block.entity.renderer.PackageBlockEntityRenderer;

import bee.post.block.entity.renderer.ParcelShelfBlockEntityRenderer;
import bee.post.client.renderer.ParcelShelfSpecialRenderer;
import bee.post.client.renderer.PlayerStampSpecialRenderer;
import bee.post.registry.MailBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;

public class SpamtonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(MailBlockEntities.PACKAGE_BLOCK_ENTITY, PackageBlockEntityRenderer::new);

        BlockEntityRenderers.register(MailBlockEntities.PARCEL_SHELF_BLOCK_ENTITY, ParcelShelfBlockEntityRenderer::new);

        SpecialModelRenderers.ID_MAPPER.put(Spamton.id("player_stamp"), PlayerStampSpecialRenderer.Unbaked.MAP_CODEC);



    }
}
