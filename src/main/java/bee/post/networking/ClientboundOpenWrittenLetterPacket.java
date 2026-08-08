package bee.post.networking;

import bee.post.Spamton;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.component.WrittenBookContent;

public record ClientboundOpenWrittenLetterPacket(WrittenBookContent writtenBookContent, int hand) implements CustomPacketPayload {

    public static final Identifier IDENTIFIER = Spamton.id("clientbound_open_written_letter");
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenWrittenLetterPacket> STREAM_CODEC = StreamCodec.composite(
            WrittenBookContent.STREAM_CODEC, ClientboundOpenWrittenLetterPacket::writtenBookContent,
            ByteBufCodecs.INT, ClientboundOpenWrittenLetterPacket::hand,
            ClientboundOpenWrittenLetterPacket::new);
    public static final Type<ClientboundOpenWrittenLetterPacket> TYPE = new Type<>(IDENTIFIER);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
