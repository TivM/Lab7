package common.exceptions;

public class PermissionDeniedException extends CommandException{
    public PermissionDeniedException() {
        super("access denied");
    }
}
