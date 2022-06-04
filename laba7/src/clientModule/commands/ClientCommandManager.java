package clientModule.commands;

import clientModule.client.Client;
import common.commands.Command;
import common.commands.CommandManager;
import common.connection.AnswerMsg;
import common.connection.Request;
import common.connection.Status;
import common.data.User;
import common.exceptions.ConnectionException;
import common.exceptions.ConnectionTimeoutException;
import common.exceptions.InvalidDataException;

import static common.io.OutputManager.*;
import static common.io.OutputManager.print;

/**
 * command manager for client
 */
public class ClientCommandManager extends CommandManager {
    private Client client;
    private User user = null;
    public ClientCommandManager(Client c){
        client = c;
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new HelpCommand());
    }

    public Client getClient(){
        return client;
    }

    @Override
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        if (hasCommand(msg)){
            Command cmd =  getCommand(msg);
            cmd.setArgument(msg);
            res = (AnswerMsg)cmd.run();
        } else {
            try{
                if (user != null && (!msg.getCommandName().equals("sign_in") && !msg.getCommandName().equals("sign_up"))) {
                    msg.setUser(user);
                }
                client.send(msg);
                res = (AnswerMsg) client.receive();
                if (user == null) user = msg.getUser();
                if (msg.getCommandName().equals("log_out")) user = null;
                if (msg.getCommandName().equals("exit")) res.setStatus(Status.EXIT);
            }
            catch (ConnectionTimeoutException e){
                res.info("no attempts left, shutting down").setStatus(Status.EXIT);
            }
            catch(InvalidDataException | ConnectionException e){
                res.error(e.getMessage());
            }
        }
        print(res);
        return res;
    }
}
