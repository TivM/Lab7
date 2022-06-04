package serverModule.utility;

import common.connection.BufferPacket;
import common.exceptions.ClosedConnectionException;
import common.exceptions.ConnectionException;
import common.exceptions.InvalidDataException;
import common.exceptions.InvalidReceivedDataException;
import serverModule.log.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.RecursiveTask;

public class RequestReceiver extends RecursiveTask<BufferPacket> {
    private InetSocketAddress clientAddress;
    private DatagramChannel channel;
    public final int BUFFER_SIZE = 4096;

    public RequestReceiver(InetSocketAddress clientAddress, DatagramChannel channel) {
        this.clientAddress = clientAddress;
        this.channel = channel;
    }

    @Override
    protected BufferPacket compute() {
        try {
            return receive();
        } catch (ConnectionException | InvalidDataException e) {
            Log.logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * receives request from client
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public BufferPacket receive() throws ConnectionException, InvalidDataException{
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            buf.flip();
            Log.logger.trace("received request from " + clientAddress.toString());
        }catch (ClosedChannelException e){
            throw new ClosedConnectionException();
        } catch(IOException e){
            throw new ConnectionException("something went wrong during receiving request");
        }
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            BufferPacket bp  = (BufferPacket) objectInputStream.readObject();
            objectInputStream.close();
            bp.setRemote_address(clientAddress);
            return bp;
        } catch(ClassNotFoundException|ClassCastException|IOException e){
            throw new InvalidReceivedDataException();
        }
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }
}
