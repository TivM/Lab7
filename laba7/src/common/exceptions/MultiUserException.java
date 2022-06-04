package common.exceptions;

public class MultiUserException extends CommandException{
    public MultiUserException() {
        super("the user is logged in");
    }
}
