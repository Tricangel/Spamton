package bee.post.networking;

import bee.post.Spamton;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.WritableBookContent;

public record ClientboundOpenWritableLetterPacket(WritableBookContent writableBookContent, int hand) implements CustomPacketPayload {

    public static final Identifier IDENTIFIER = Spamton.id("clientbound_open_writable_letter");
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenWritableLetterPacket> STREAM_CODEC = StreamCodec.composite(
            WritableBookContent.STREAM_CODEC, ClientboundOpenWritableLetterPacket::writableBookContent,
            ByteBufCodecs.INT, ClientboundOpenWritableLetterPacket::hand,
            ClientboundOpenWritableLetterPacket::new);
    public static final CustomPacketPayload.Type<ClientboundOpenWritableLetterPacket> TYPE = new CustomPacketPayload.Type<>(IDENTIFIER);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
