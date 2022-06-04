package common.exceptions;

public class NoUserException extends CommandException {
    public NoUserException() {
        super("the user doesn't exist");
    }
}

