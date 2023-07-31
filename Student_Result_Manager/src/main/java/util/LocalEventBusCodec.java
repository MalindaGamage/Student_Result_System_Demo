package util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class LocalEventBusCodec<T> implements MessageCodec<T, T> {
    private final Class<T> aClass;

    public LocalEventBusCodec(Class<T> aClass) {
        this.aClass = aClass;
    }

    public void encodeToWire(Buffer buffer, T o) {
    }

    public T decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    public T transform(T o) {
        return o;
    }

    public String name() {
        return this.aClass.getName() + "Codec";
    }

    public byte systemCodecID() {
        return -1;
    }
}
