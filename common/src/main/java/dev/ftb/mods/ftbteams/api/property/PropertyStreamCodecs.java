package dev.ftb.mods.ftbteams.api.property;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/// 26.3: FriendlyByteBuf lost readList/readCollection/writeCollection/readMap/writeMap.
/// These codecs use the same wire format (VarInt size followed by the elements / key-value pairs).
final class PropertyStreamCodecs {
    static final StreamCodec<ByteBuf, List<String>> STRING_LIST = ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list());
    static final StreamCodec<ByteBuf, Set<String>> STRING_SET = ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.STRING_UTF8);

    private PropertyStreamCodecs() {
    }

    /// toNet may be null when the codec is only used for decoding
    static <T> StreamCodec<FriendlyByteBuf, Map<String, T>> stringMap(BiConsumer<FriendlyByteBuf, T> toNet, Function<FriendlyByteBuf, T> fromNet) {
        return ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, StreamCodec.of((b, v) -> toNet.accept(b, v), b -> fromNet.apply(b)));
    }
}
