package bee.post;

import bee.post.block.entity.renderer.PackageBlockEntityRenderer;
import bee.post.block.entity.renderer.ParcelShelfBlockEntityRenderer;
import bee.post.client.model.ParcelModel;
import bee.post.client.model.PlayerStampModel;
import bee.post.client.overlay.DispenserOverlay;
import bee.post.client.overlay.PeekPostalInfoOverlay;
import bee.post.client.renderer.PlayerStampSpecialRenderer;
import bee.post.client.screen.LetterEditScreen;
import bee.post.client.screen.LetterViewScreen;
import bee.post.entity.renderer.PackageEntityRenderer;
import bee.post.item.component.ClientParcelContentsTooltip;
import bee.post.item.component.ParcelContents;
import bee.post.networking.ClientboundOpenWritableLetterPacket;
import bee.post.networking.ClientboundOpenWrittenLetterPacket;
import bee.post.registry.MailBlockEntities;
import bee.post.registry.MailEntityTypes;
import bee.post.registry.MailItemComponents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.world.InteractionHand;

public class SpamtonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(MailBlockEntities.PACKAGE_BLOCK_ENTITY, PackageBlockEntityRenderer::new);
        ComponentTooltipAppenderRegistry.addFirst(MailItemComponents.POSTAL_INFO);
        BlockEntityRenderers.register(MailBlockEntities.PARCEL_SHELF_BLOCK_ENTITY, ParcelShelfBlockEntityRenderer::new);

        HudElementRegistry.addFirst(Spamton.id("peek_parcel_shelf"), new PeekPostalInfoOverlay());
        HudElementRegistry.addFirst(Spamton.id("dispenser_overlay"), new DispenserOverlay());
        EntityModelLayerRegistry.registerModelLayer(PlayerStampModel.STAMP, PlayerStampModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PlayerStampModel.STAMP_ITEM, PlayerStampModel::createItemLayer);
        EntityModelLayerRegistry.registerModelLayer(ParcelModel.PARCEL, ParcelModel::createBodyLayer);

        EntityRenderers.register(MailEntityTypes.PACKAGE, PackageEntityRenderer::new);

        SpecialModelRenderers.ID_MAPPER.put(Spamton.id("player_stamp"), PlayerStampSpecialRenderer.Unbaked.MAP_CODEC);

        TooltipComponentCallback.EVENT.register(component -> {
            if (component instanceof ParcelContents contents) {
                return new ClientParcelContentsTooltip(contents);
            }
            return null;
        });

        ClientPlayNetworking.registerGlobalReceiver(ClientboundOpenWritableLetterPacket.TYPE, ((packet, context) -> {

                InteractionHand hand = packet.hand() == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

                context.client().setScreen(new LetterEditScreen(context.player(), context.player().getItemInHand(hand), hand, packet.writableBookContent()));

        }));

        ClientPlayNetworking.registerGlobalReceiver(ClientboundOpenWrittenLetterPacket.TYPE, ((packet, context) -> {

            context.client().setScreen(new LetterViewScreen(LetterViewScreen.LetterAccess.fromContent(packet.writtenBookContent())));

        }));

    }

}
