package serverModule.commands;

import serverModule.collection.CollectionManager;
import serverModule.collection.DatabaseCollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.User;
import common.data.Worker;
import common.data.WorkerFull;
import common.exceptions.*;

public class ClearCommand extends CommandImpl{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public ClearCommand(CollectionManager cm, DatabaseCollectionManager dm){
        super("clear",CommandType.NORMAL);
        collectionManager = cm;
        databaseCollectionManager = dm;
    }

    @Override
    public String execute() throws InvalidDataException, DatabaseManagerException {
        User user = getUserArg();
        if (user == null) throw new NonAuthorizedException();
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        for (WorkerFull workerFull : collectionManager.getCollection()) {
            if (!workerFull.getUser().equals(user)) throw new PermissionDeniedException();
        }
        databaseCollectionManager.clearCollection();
        collectionManager.clear();
        return "collection cleared";
    }

}
