package serverModule.utility;

import serverModule.commands.ServerCommandManager;
import common.commands.CommandType;
import common.connection.AnswerMsg;
import common.connection.Request;
import common.exceptions.CommandException;
import common.exceptions.NonAuthorizedException;
import serverModule.exceptions.ServerOnlyCommandException;
import serverModule.log.Log;

import java.time.ZonedDateTime;

public class RequestProcessingThread extends Thread{
    private Request commandMsg;
    private ServerCommandManager commandManager;
    private AnswerMsg answerMsg;

    public RequestProcessingThread(Request commandMsg, ServerCommandManager commandManager, AnswerMsg answerMsg) {
        this.commandMsg = commandMsg;
        this.commandManager = commandManager;
        this.answerMsg = answerMsg;
    }

    @Override
    public void run() {
        try {
            if (commandMsg.getUser() == null && !commandMsg.getCommandName().equals("sign_in") && !commandMsg.getCommandName().equals("sign_up") && !commandMsg.getCommandName().equals("exit")) {
                throw new NonAuthorizedException();
            }
            if (commandMsg.getWorker() != null) {
                commandMsg.getWorker().setCreationDate(ZonedDateTime.now());
            }
            if (commandManager.getCommand(commandMsg).getType() == CommandType.SERVER_ONLY) {
                throw new ServerOnlyCommandException();
            }
            answerMsg = commandManager.runCommand(commandMsg);
        } catch (CommandException e) {
            answerMsg.error(e.getMessage());
            Log.logger.error(e.getMessage());
        }
    }

    public AnswerMsg getAnswerMsg() {
        return answerMsg;
    }
}
