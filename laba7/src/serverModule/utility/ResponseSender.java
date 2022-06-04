package serverModule.utility;

import common.connection.AnswerMsg;
import common.connection.Response;
import common.exceptions.ConnectionException;
import common.exceptions.InvalidAddressException;
import serverModule.log.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ResponseSender implements Runnable{
    private AnswerMsg answerMsg;
    private InetSocketAddress clientAddress;
    private DatagramChannel channel;
    public final int BUFFER_SIZE = 4096;

    public ResponseSender(AnswerMsg answerMsg, InetSocketAddress clientAddress, DatagramChannel channel) {
        this.answerMsg = answerMsg;
        this.clientAddress = clientAddress;
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            send(splitAnswer(answerMsg));
        } catch (ConnectionException e) {
            Log.logger.error(e.getMessage());
        }
    }

    public AnswerMsg[] splitAnswer (AnswerMsg answerMsg){
        int substringSize = 256;
        int totalSubstrings = (int) Math.ceil((double)answerMsg.getMessage().length()/substringSize);
        String[] s = new String[totalSubstrings];
        int index = 0;
        for(int i=0; i < answerMsg.getMessage().length(); i = i + substringSize){
            s[index++] =
                    answerMsg.getMessage().substring(i, Math.min(i + substringSize, answerMsg.getMessage().length()));
        }
        AnswerMsg[] answerMsgs = new AnswerMsg[s.length];
        for (int i = 0; i < s.length; i++) {
            answerMsgs[i] = new AnswerMsg();
            answerMsgs[i].info(s[i]);
            answerMsgs[i].setStatus(answerMsg.getStatus());
            answerMsgs[i].setFinish(false);
        }
        answerMsgs[answerMsgs.length-1].setFinish(true);
        return answerMsgs;
    }

    /**
     * sends response
     * @param responses
     * @throws ConnectionException
     */
    public void send(Response[] responses)throws ConnectionException{
        if (clientAddress == null) throw new InvalidAddressException("no client address found");
        for (Response response : responses)
            try{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(response);
                channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
                System.out.println("packet sent, packets size = "+ responses.length);
                Log.logger.trace("sent response to " + clientAddress.toString());
            } catch(IOException e){
                throw new ConnectionException("something went wrong during sending response");
            }
    }
}
