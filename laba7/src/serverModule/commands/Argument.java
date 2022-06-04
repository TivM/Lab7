package serverModule.commands;

import common.data.User;
import common.data.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class Argument implements common.connection.Request{
    private String arg;
    private Worker worker;
    private User user;
    public Argument(String s, Worker w, User u){
        arg = s;
        worker = w;
        user = u;
    }
    public String getStringArg(){
        return arg;
    }
    public Worker getWorker(){
        return worker;
    }
    public String getCommandName(){
        return "";
    }
    public User getUser() {return user;}
    public void setUser(Serializable u) {user=(User) u;}
}
