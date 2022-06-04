package common.connection;

import common.data.User;
import common.data.Worker;

import java.io.Serializable;

public interface Request extends Serializable{
    public String getStringArg();
    public Worker getWorker();
    public String getCommandName();
    public User getUser();
    public void setUser(Serializable user);
}
