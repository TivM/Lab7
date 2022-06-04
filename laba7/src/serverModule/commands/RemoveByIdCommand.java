package serverModule.commands;

import common.data.User;
import serverModule.collection.CollectionManager;
import serverModule.collection.DatabaseCollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.*;

import static common.utils.Parser.parseId;
public class RemoveByIdCommand extends CommandImpl{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    public RemoveByIdCommand(CollectionManager cm, DatabaseCollectionManager dm){
        super("remove_by_id",CommandType.NORMAL);
        collectionManager = cm;
        databaseCollectionManager = dm;
    }


    @Override
    public String execute() throws InvalidDataException, DatabaseManagerException {
        User user = getUserArg();
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArg()) throw new MissedCommandArgumentException();
        Long id = parseId(getStringArg());
        if(!databaseCollectionManager.checkWorkerByIdAndUserId(id,user)) throw new InvalidCommandArgumentException("no such id");
        databaseCollectionManager.deleteWorkerById(id);
        boolean success = collectionManager.removeByID(id);
        if (success) return "element #" + id + " removed";
        else throw new CommandException("cannot remove");
    }
    
}
