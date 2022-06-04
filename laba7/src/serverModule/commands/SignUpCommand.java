package serverModule.commands;

import serverModule.collection.DatabaseUserManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.User;
import common.exceptions.*;

public class SignUpCommand extends CommandImpl {
    private DatabaseUserManager databaseUserManager;
    public SignUpCommand(DatabaseUserManager dm) {
        super("sign_up", CommandType.NORMAL);
        databaseUserManager = dm;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException, DatabaseManagerException {
        User user = getUserArg();
        if (user == null) throw new MissedCommandArgumentException();
        if (!databaseUserManager.insertUser(user)) {
            throw new UserAlreadyExistsException();
        }
        return "New user signed up";
    }
}
