package serverModule.commands;


import serverModule.collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.data.WorkerFull;
import common.exceptions.EmptyCollectionException;
import common.exceptions.InvalidCommandArgumentException;
import common.exceptions.MissedCommandArgumentException;

import java.util.List;
public class FilterGreaterThanSalaryCommand extends CommandImpl{
    private CollectionManager collectionManager;
    public FilterGreaterThanSalaryCommand(CollectionManager cm){
        super("filter_greater_than_salary",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        Long salary = 0L;
        if(!hasStringArg()) throw new MissedCommandArgumentException();
        if (collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        try{
            salary = Long.parseLong(getStringArg());
        } catch (NumberFormatException e){
            throw new InvalidCommandArgumentException("id must be Long");
        }
        List<WorkerFull> list = collectionManager.printGreaterThanSalary(Long.parseLong(getStringArg()));
        if(list.isEmpty()) return "no one greater";
        String s = list.stream()
                .sorted(new WorkerFull.SortingComparator())
                .map(e -> e.toString()).reduce("", (a,b)->a + b + "\n");
        return s;
    }
}

