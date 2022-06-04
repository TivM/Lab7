package common.connection;

import java.io.Serializable;
import java.net.SocketAddress;

public class BufferPacket implements Serializable {
    private byte[] byteBuffer;
    private boolean finish;
    private SocketAddress remote_address;

    public BufferPacket(byte[] byteBuffer, boolean finish) {
        this.byteBuffer = byteBuffer;
        this.finish = finish;
        this.remote_address = null;
    }

    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public SocketAddress getRemote_address() {
        return remote_address;
    }

    public void setRemote_address(SocketAddress remote_address) {
        this.remote_address = remote_address;
    }
}
