package serverModule.commands;

import common.commands.CommandImpl;
import common.commands.CommandType;
import common.exceptions.ExitException;
public class ExitCommand extends CommandImpl {
    public ExitCommand(){
        super("exit_server", CommandType.NORMAL);
    }
    @Override 
    public String execute(){
        throw new ExitException();
    }

}
