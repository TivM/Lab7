package serverModule.server;

import serverModule.collection.*;
import serverModule.commands.ServerCommandManager;
import common.commands.Commandable;
import common.connection.*;
import common.exceptions.*;
import common.file.FileManager;
import common.file.ReaderWriter;
import common.exceptions.NonAuthorizedException;
import serverModule.log.Log;
import serverModule.utility.RequestProcessingThread;
import serverModule.utility.RequestReceiver;
import serverModule.utility.ResponseSender;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * server class
 */
public class Server extends Thread implements SenderReceiver {

    private CollectionManager collectionManager;
    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;
    private DatabaseCollectionManager databaseCollectionManager;
    private ReaderWriter fileManager;
    private ServerCommandManager commandManager;
    private int port;
    private InetSocketAddress clientAddress;
    private DatagramChannel channel;
    protected ForkJoinPool requestReceiverPool;
    protected ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private HashMap<SocketAddress, byte[]> packets;


    private volatile boolean running;

    private void init(int p, String path) throws ConnectionException{
        running=true;
        port = p;
        databaseManager = new DatabaseManager();
        databaseUserManager = new DatabaseUserManager(databaseManager);
        databaseCollectionManager = new DatabaseCollectionManager(databaseManager, databaseUserManager);
        collectionManager = new WorkerCollectionManager(databaseCollectionManager);
        fileManager = new FileManager(path);
        commandManager = new ServerCommandManager(this);
        requestReceiverPool = ForkJoinPool.commonPool();
        packets = new HashMap<>();
        host(port);
        setName("server thread");
        Log.logger.trace("starting server");
    }

    private void host(int p) throws ConnectionException{
        try{
            if(channel!=null && channel.isOpen()) channel.close();
            channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(port));
        }
        catch(AlreadyBoundException e){
            throw new PortAlreadyInUseException();
        }
        catch(IllegalArgumentException e){
            throw new InvalidPortException();
        }
        catch(IOException e){
            throw new ConnectionException("something went wrong during server initialization");
        }
    }

    public Server(int p, String path) throws ConnectionException{
        init(p,path);
    }

    /**
     * runs server
     */
    public void run() {
        while (running) {
            AnswerMsg answerMsg = new AnswerMsg();
            try {
                try {
                    RequestReceiver requestReceiver = new RequestReceiver(clientAddress, channel);
                    BufferPacket bp = requestReceiverPool.invoke(requestReceiver);
                    this.clientAddress = requestReceiver.getClientAddress();
                    if (packets.containsKey(bp.getRemote_address())) {
                        packets.put(bp.getRemote_address(), joinByteArray(packets.get(bp.getRemote_address()), bp.getByteBuffer()));
                    } else {
                        packets.put(bp.getRemote_address(), bp.getByteBuffer());
                    }
                    if (!bp.isFinish()) continue;
                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(packets.get(bp.getRemote_address())));
                    Request commandMsg = (Request) objectInputStream.readObject();
                    objectInputStream.close();
                    packets.remove(bp.getRemote_address());
                    RequestProcessingThread processingThread = new RequestProcessingThread(commandMsg, commandManager, answerMsg);
                    processingThread.start();
                    try {
                        processingThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    answerMsg = processingThread.getAnswerMsg();
                    if (answerMsg.getStatus() == Status.EXIT) {
                        close();
                    }
                } catch (CommandException e) {
                    answerMsg.error(e.getMessage());
                    Log.logger.error(e.getMessage());
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
                cachedThreadPool.submit(new ResponseSender(answerMsg, clientAddress, channel));
            } catch (NonAuthorizedException e) {
                Log.logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        cachedThreadPool.shutdown();
    }

    private byte[] joinByteArray(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

    public void consoleMode(){
        commandManager.consoleMode();
    }

    /**
     * close server and connection
     */
    public void close(){
        try{
            running=false;
            channel.close();
        } catch (IOException e){
            Log.logger.error("cannot close channel");
        }
    }

    public Commandable getCommandManager(){
        return commandManager;
    }
    public ReaderWriter getFileManager(){
        return fileManager;
    }
    public CollectionManager getCollectionManager(){
        return collectionManager;
    }
    public DatabaseCollectionManager getDatabaseCollectionManager() {
        return databaseCollectionManager;
    }
    public DatabaseUserManager getDatabaseUserManager() {
        return databaseUserManager;
    }
}