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
        // Read declared length
        int length = readVarInt();

        // Check if declared length is longer than maximum allowed
        if (length > max * 3)
            return null;

        // Check if declared length is less than zero
        if (length < 0)
            return null;

        // Read the string
        String string = source.toString(source.readerIndex(), length, StandardCharsets.UTF_8);

        // Update reader index, so we will not read the same data again
        source.readerIndex(source.readerIndex() + length);

        // Check if received string is longer than maximum allowed
        if (string.length() > max)
            return null;

        return string;
    }

    // Used to read ping coords
    public double readDouble() {
        return source.readDouble();
    }

    // Used to check if ping has entity UUID attached
    public boolean readBoolean() {
        return source.readBoolean();
    }

    // Used to read entity uuid
    public UUID readUUID() {
        return new UUID(
                source.readLong(),
                source.readLong()
        );
    }

    // Used to read string
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
