package common.exceptions;

public class UserAlreadyExistsException extends CommandException{
    public UserAlreadyExistsException(){
        super("user is already exist");
    }
}
