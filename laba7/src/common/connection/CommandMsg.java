package common.connection;

import common.data.User;
import common.data.Worker;

import java.io.Serializable;

/**
 * Message witch include command and arguments
 */
public class CommandMsg implements Request{
    private String commandName;
    private String commandStringArgument;
    private Serializable commandObjectArgument;
    private Serializable commandUserArgument;

    public CommandMsg(String commandNm, String commandSA, Serializable commandOA, Serializable commandUA) {
        commandName = commandNm;
        commandStringArgument = commandSA;
        commandObjectArgument = commandOA;
        commandUserArgument = commandUA;
    }

    /**
     * @return Command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Command string argument.
     */
    public String getStringArg() {
        return commandStringArgument;
    }

    /**
     * @return Command object argument.
     */
    public Worker getWorker() {
        return (Worker) commandObjectArgument;
    }

    /**
     * @return Command user argument.
     */
    public User getUser() {
        return (User) commandUserArgument;
    }

    @Override
    public void setUser(Serializable commandUserArgument) {
        this.commandUserArgument = commandUserArgument;
    }
}