package common.commands;

import common.connection.AnswerMsg;
import common.connection.Request;
import common.connection.Response;
import common.connection.Status;
import common.data.User;
import common.data.Worker;
import common.exceptions.*;

/**
 * basic command implementation
 */
public abstract class CommandImpl implements Command{
    private CommandType type;
    private String name;
    private Request arg;
    public CommandImpl(String n, CommandType t){
        name = n;
        type = t;
    }
    public CommandType getType(){
        return type;
    }
    public String getName(){
        return name;
    }

    /**
     * custom execute command
     * @return
     * @throws InvalidDataException
     * @throws CommandException
     * @throws ConnectionException
     */
    public abstract String execute() throws InvalidDataException, CommandException, ConnectionException, DatabaseManagerException, WrongAmountOfParametersException, NonAuthorizedException, FileException;

    /**
     * wraps execute into response
     * @return
     */
    public Response run(){
        AnswerMsg res = new AnswerMsg();
        try{
            res.info(execute());
        }
        catch(ExitException e){
            res.info(e.getMessage());
            res.setStatus(Status.EXIT);
        }
        catch(InvalidDataException | CommandException | ConnectionException | DatabaseManagerException | WrongAmountOfParametersException | FileException e){
            res.error(e.getMessage());
        }
        return res;
    }
    public Request getArgument(){
        return arg;
    }
    public void setArgument(Request req){
        arg=req;
    }
    public boolean hasStringArg(){
        return arg!=null && arg.getStringArg()!=null && !arg.getStringArg().equals("");
    }

    public boolean hasWorkerArg(){
        return arg!=null && arg.getWorker()!=null;
    }

    public String getStringArg(){
        return getArgument().getStringArg();
    }

    public Worker getWorkerArg(){
        return getArgument().getWorker();
    }

    public User getUserArg() {
        return getArgument().getUser();
    }
}
