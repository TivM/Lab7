package clientModule.client;

import clientModule.commands.ClientCommandManager;
import common.connection.*;
import common.data.User;
import common.exceptions.*;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static common.io.OutputManager.printErr;

/**
 * client class
 */
public class Client extends Thread implements SenderReceiver {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 1000;
    public final int MAX_ATTEMPTS = 3;

    private boolean running;
    private ClientCommandManager commandManager;

    /**
     * initialize client
     *
     * @param addr
     * @param p
     * @throws ConnectionException
     */
    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        running = true;
        commandManager = new ClientCommandManager(this);
        setName("client thread");
    }

    public Client(String addr, int p) throws ConnectionException {
        init(addr, p);
    }

    /**
     * connects client to server
     *
     * @param addr
     * @param p
     * @throws ConnectionException
     */
    public void connect(String addr, int p) throws ConnectionException {
        try {
            address = new InetSocketAddress(InetAddress.getByName(addr), p);
        } catch (UnknownHostException e) {
            throw new InvalidAddressException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        }
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (IOException e) {
            throw new ConnectionException("cannot open socket");
        }
    }

    /**
     * sends request to server
     *
     * @param request
     * @throws ConnectionException
     */
    public void send(Request request) throws ConnectionException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(request);
            objOutput.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            byte[][] msg = divideArray(data, 2048);
            for (int i = 0; i < msg.length; i++) {
                BufferPacket bp;
                if (i + 1 == msg.length) bp = new BufferPacket(msg[i], true);
                else bp = new BufferPacket(msg[i], false);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(bp);
                oos.flush();
                DatagramPacket requestPacket = new DatagramPacket(bos.toByteArray(), bos.size(), address);
                socket.send(requestPacket);
                bos.close();
            }
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while sending request");
        }
    }

    private byte[][] divideArray(byte[] source, int chunksize) {
        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];
        int start = 0;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + chunksize);
            start += chunksize;
        }
        return ret;
    }

    /**
     * receive message from server
     *
     * @return
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public Response receive() throws ConnectionException, InvalidDataException {

        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        Response response = new AnswerMsg();
        while (true) {
            try {
                socket.receive(receivePacket);
            } catch (SocketTimeoutException e) {
                int attempts = MAX_ATTEMPTS;
                boolean success = false;
                for (; attempts > 0; attempts--) {
                    printErr("server response timeout exceeded, trying to reconnect. " + Integer.toString(attempts) + " attempts left");
                    try {
                        socket.receive(receivePacket);
                        success = true;
                        break;
                    } catch (IOException error) {

                    }
                }

                throw new ConnectionTimeoutException();
            } catch (IOException e) {
                throw new ConnectionException("something went wrong while receiving response");
            }

            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
                //return (Response) objectInputStream.readObject();
                response = joinResponses(response, (Response) objectInputStream.readObject());
            } catch (ClassNotFoundException | ClassCastException | IOException e) {
                System.err.println("packet = " + response.getMessage());
                e.printStackTrace();
                //throw new InvalidReceivedDataException();
            }
            if (response.isFinish())
                break;
        }
        return response;
    }

    public Response joinResponses(Response r1, Response r2) {
        AnswerMsg answerMsg = new AnswerMsg();
        answerMsg.info(r1.getMessage() + r2.getMessage());
        answerMsg.setStatus(r2.getStatus());
        answerMsg.setFinish(r2.isFinish());
        return answerMsg;
    }

    /**
     * runs client until interrupt
     */
    @Override
    public void run() {
        commandManager.consoleMode();
        close();
    }

    /**
     * close client
     */
    public void close() {
        running = false;
        commandManager.close();
        socket.close();
    }
}