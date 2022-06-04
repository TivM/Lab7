package serverModule.commands;

import serverModule.collection.CollectionManager;
import serverModule.collection.DatabaseCollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.data.WorkerFull;
import common.exceptions.CommandException;
import common.exceptions.DatabaseManagerException;

public class AddIfMinCommand extends CommandImpl{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddIfMinCommand(CollectionManager cm, DatabaseCollectionManager dm){
        super("add_if_min",CommandType.NORMAL);
        collectionManager = cm;
        databaseCollectionManager = dm;
    }

    @Override
    public String execute() throws DatabaseManagerException {
        WorkerFull workerToCompare = new WorkerFull(
                collectionManager.generateNextId(),
                getWorkerArg().getName(),
                getWorkerArg().getCoordinates(),
                getWorkerArg().getCreationDate(),
                getWorkerArg().getSalary(),
                getWorkerArg().getEndDate(),
                getWorkerArg().getPosition(),
                getWorkerArg().getStatus(),
                getWorkerArg().getPerson(),
                getUserArg()
        );
        boolean success = collectionManager.addIfMin(workerToCompare);
        if (success) {
            collectionManager.add(databaseCollectionManager.insertWorker(getWorkerArg(), getUserArg()));
            return ("Added element: " + getWorkerArg().toString());
        }
        else throw new CommandException("cannot add (most often it happens because you forgot to specify the salary)");
    }
    
}
