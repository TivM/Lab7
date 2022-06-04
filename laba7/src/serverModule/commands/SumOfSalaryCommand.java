package serverModule.commands;

import serverModule.collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.*;

public class SumOfSalaryCommand extends CommandImpl {
    private CollectionManager collectionManager;
    public SumOfSalaryCommand(CollectionManager cm){
        super("sum_of_salary", CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException, CommandException, FileException, ConnectionException {
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        return "Sum of salary: " + collectionManager.sumOfSalary().toString();
    }
}
