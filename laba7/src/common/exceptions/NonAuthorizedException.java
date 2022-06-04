package common.exceptions;

public class NonAuthorizedException extends CommandException {
    public NonAuthorizedException() {
        super("non authorized user");
    }
}
