package bee.post.client.networking;

import bee.post.Spamton;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public record ServerboundSignLetterPacket(int slot, List<String> pages, String title, String author) implements CustomPacketPayload {
    public static final Identifier IDENTIFIER = Spamton.id("serverbound_edit_letter");
    public static final StreamCodec<ByteBuf, ServerboundSignLetterPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ServerboundSignLetterPacket::slot,
            ByteBufCodecs.stringUtf8(1024).apply(ByteBufCodecs.list(100)), ServerboundSignLetterPacket::pages,
            ByteBufCodecs.stringUtf8(32), ServerboundSignLetterPacket::title,
            ByteBufCodecs.stringUtf8(32), ServerboundSignLetterPacket::author,
            ServerboundSignLetterPacket::new);
    public static final Type<ServerboundSignLetterPacket> TYPE = new Type<>(IDENTIFIER);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
