package serverModule.commands;

import serverModule.collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.*;

public class AverageOfSalaryCommand extends CommandImpl {
    private CollectionManager collectionManager;
    public AverageOfSalaryCommand(CollectionManager cm){
        super("average_of_salary", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        return "Average of salary: " + collectionManager.averageOfSalary().toString();
    }
}

