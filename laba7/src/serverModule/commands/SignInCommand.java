package serverModule.commands;

import serverModule.collection.DatabaseUserManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.User;
import common.exceptions.*;

public class SignInCommand extends CommandImpl {
    DatabaseUserManager databaseUserManager;
    public SignInCommand(DatabaseUserManager dm) {
        super("sign_in", CommandType.NORMAL);
        databaseUserManager = dm;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException, DatabaseManagerException {
        User user = getUserArg();
        if (user == null) throw new MissedCommandArgumentException();
        if (databaseUserManager.checkUserByUsernameAndPassword(user)) {
            databaseUserManager.updateOnline(user, true);
            return "Authorization was successful";
        } else {
            throw new NoUserException();
        }
    }
}
