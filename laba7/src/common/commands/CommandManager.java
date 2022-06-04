package common.commands;

import common.connection.CommandMsg;
import common.connection.Response;
import common.connection.Status;
import common.exceptions.FileException;
import common.exceptions.NoSuchCommandException;
import common.io.ConsoleInputManager;
import common.io.FileInputManager;
import common.io.InputManager;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static common.io.OutputManager.print;

public abstract class CommandManager implements Commandable,Closeable{
    private Map<String,Command> map;
    private InputManager inputManager;
    private boolean isRunning;
    private String currentScriptFileName;
    private static Stack<String> callStack = new Stack<>();

    public void clearStack(){
        callStack.clear();
    }
    public Stack<String> getStack(){
        return callStack;
    }
    public String getCurrentScriptFileName(){
        return currentScriptFileName;
    }
    public CommandManager(){
        isRunning = false;
        currentScriptFileName = "";
        map = new HashMap<String,Command>();
    }
    public void addCommand(Command c) {
        map.put(c.getName(),c);
    }
    public void addCommand(String key, Command c){
        map.put(key, c);
    }

    public Command getCommand(String s){
        if (! hasCommand(s)) throw new NoSuchCommandException();
        Command cmd =  map.get(s);
        return cmd;
    }
    public boolean hasCommand(String s){
        return map.containsKey(s);
    }

    public void consoleMode(){
        inputManager = new ConsoleInputManager();
        isRunning = true;
        while(isRunning){
            print("enter command (help to get command list): ");
            CommandMsg commandMsg = inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if(answerMsg.getStatus()==Status.EXIT) {
                close();
            }
        }
    }
    public void fileMode(String path) throws FileException{
        currentScriptFileName = path;
        inputManager = new FileInputManager(path);
        isRunning = true;
        while(isRunning && inputManager.getScanner().hasNextLine()){
            CommandMsg commandMsg= inputManager.readCommand();
            Response answerMsg = runCommand(commandMsg);
            if(answerMsg.getStatus()==Status.EXIT) {
                close();
            }
        }
    }

    public void setInputManager(InputManager in){
        inputManager = in;
    }
    public InputManager getInputManager(){
        return inputManager;
    }

    public static String getHelp() {
        return "\r\nhelp : show help for available commands\r\n\r\n" +
                "info : Write to standard output information about the collection " + "(type,\r\n" +
                "initialization date, number of elements, etc.)\r\n\r\n" +
                "show : print to standard output all elements of the collection in\r\n" +
                "string representation\r\n\r\n" +
                "add {element} : add a new element to the collection\r\n\r\n" +
                "update id {element} : update the value of the collection element whose id\r\n" +
                "equal to given\r\n\r\n" +
                "remove_by_id id : remove an element from the collection by its id\r\n\r\n" +
                "clear : clear the collection\r\n\r\n" +
                "load (file_name - optional): load collection from file\r\n\r\n" +
                "execute_script file_name : read and execute script from specified file.\r\n" +
                "The script contains commands in the same form in which they are entered\r\n" +
                "user is interactive.\r\n\r\n" +
                "exit : exit the program (without saving to a file)\r\n\r\n" +
                "add_if_min {element} : add a new element to the collection if it\r\n" +
                "the value is less than the smallest element of this collection\r\n\r\n" +
                "the value is bigger than the biggest element of this collection\r\n\r\n" +
                "sum_of_salary : print the sum of the values of the salary field for all\r\n" +
                "items in the collection\r\n\r\n" +
                "average_of_salary : print the average value of the salary field for all\r\n" +
                "items in the collection\r\n\r\n" +
                "filter_greater_than_salary salary : output elements whose salary field value\r\n" +
                "is greater than the specified one\r\n";
    }
    public boolean isRunning(){
        return isRunning;
    }
    public void setRunning(boolean running){
        isRunning = running;
    }
    public void close(){
        setRunning(false);
    }
}
