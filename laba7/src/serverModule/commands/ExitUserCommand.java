package serverModule.commands;

import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.User;
import common.exceptions.*;
import serverModule.collection.DatabaseUserManager;

public class ExitUserCommand extends CommandImpl {
    private DatabaseUserManager databaseUserManager;

    public ExitUserCommand(DatabaseUserManager databaseUserManager) {
        super("exit", CommandType.NORMAL);
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, ConnectionException, DatabaseManagerException, WrongAmountOfParametersException, NonAuthorizedException, FileException {
        User user = getUserArg();
        if (user != null) databaseUserManager.updateOnline(user, false);
        return "Goodbye";
    }
}
