package me.axerr.pingwheel.api;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FriendlyByteBuf {
    private final ByteBuf source;

    public FriendlyByteBuf(ByteBuf byteBuf) {
        this.source = byteBuf;
    }

    public String readString() {
        return readString(Short.MAX_VALUE);
    }

    public String readString(int max) {
        int length = readVarInt();

        if (length > max * 3)
            return null;

        if (length < 0)
            return null;

        String string = source.toString(source.readerIndex(), length, StandardCharsets.UTF_8);
        source.readerIndex(source.readerIndex() + length);
        if (string.length() > max)
            return null;

        return string;
    }

    public double readDouble() {
        return source.readDouble();
    }
    public boolean readBoolean() {
        return source.readBoolean();
    }
    public UUID readUUID() {
        return new UUID(
                source.readLong(),
                source.readLong()
        );
    }
    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = source.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }
}
