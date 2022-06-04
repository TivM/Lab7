package serverModule.commands;

import serverModule.collection.CollectionManager;
import serverModule.collection.DatabaseCollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.User;
import common.data.Worker;
import common.data.WorkerFull;
import common.exceptions.CommandException;
import common.exceptions.InvalidDataException;
import common.exceptions.DatabaseManagerException;
import common.exceptions.NonAuthorizedException;
import common.exceptions.WrongAmountOfParametersException;

public class AddCommand extends CommandImpl{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager cm, DatabaseCollectionManager dm){
        super("add",CommandType.NORMAL);
        collectionManager = cm;
        databaseCollectionManager = dm;
    }

    @Override
    public String execute() throws InvalidDataException, CommandException, DatabaseManagerException, WrongAmountOfParametersException, NonAuthorizedException {
        Worker worker = getWorkerArg();
        User user = getUserArg();
        if (user == null) throw new NonAuthorizedException();
        if (worker == null) throw new WrongAmountOfParametersException();
        WorkerFull workerToAdd = databaseCollectionManager.insertWorker(worker, user);
        collectionManager.add(workerToAdd);
        return "Added element: " + workerToAdd.toString();
    }
}
