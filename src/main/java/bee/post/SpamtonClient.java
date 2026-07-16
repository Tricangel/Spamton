package bee.post;

import bee.post.block.entity.renderer.PackageBlockEntityRenderer;

import bee.post.registry.MailBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class SpamtonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(MailBlockEntities.PACKAGE_BLOCK_ENTITY, PackageBlockEntityRenderer::new);




    }
}
