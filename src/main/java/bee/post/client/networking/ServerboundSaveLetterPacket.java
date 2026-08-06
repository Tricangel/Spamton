package bee.post.client.networking;

import bee.post.Spamton;
import bee.post.networking.ClientboundOpenWritableLetterPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;

public record ServerboundSaveLetterPacket(List<String> pages, int slot) implements CustomPacketPayload {
    public static final Identifier IDENTIFIER = Spamton.id("serverbound_save_letter");
    public static final StreamCodec<ByteBuf, ServerboundSaveLetterPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(1024).apply(ByteBufCodecs.list(100)), ServerboundSaveLetterPacket::pages,
            ByteBufCodecs.INT, ServerboundSaveLetterPacket::slot,
            ServerboundSaveLetterPacket::new);
    public static final Type<ServerboundSaveLetterPacket> TYPE = new Type<>(IDENTIFIER);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
