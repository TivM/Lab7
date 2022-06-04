package serverModule.commands;

import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.User;
import common.exceptions.*;
import serverModule.collection.DatabaseUserManager;

public class LogOutCommand extends CommandImpl {
    private DatabaseUserManager databaseUserManager;

    public LogOutCommand(DatabaseUserManager databaseUserManager) {
        super("log_out", CommandType.NORMAL);
        this.databaseUserManager = databaseUserManager;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, ConnectionException, DatabaseManagerException, WrongAmountOfParametersException, NonAuthorizedException, FileException {
        User user = getUserArg();
        if (user == null) throw new NonAuthorizedException();
        databaseUserManager.updateOnline(user, false);
        return "Goodbye";
    }
}
