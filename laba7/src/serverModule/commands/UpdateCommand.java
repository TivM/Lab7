package serverModule.commands;

import serverModule.collection.CollectionManager;
import serverModule.collection.DatabaseCollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.*;
import common.exceptions.*;

import java.time.ZonedDateTime;

import static common.utils.Parser.parseId;
public class UpdateCommand extends CommandImpl{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager cm, DatabaseCollectionManager dm){
        super("update",CommandType.NORMAL);
        collectionManager = cm;
        databaseCollectionManager = dm;
    }

    @Override
    public String execute() throws InvalidDataException, NonAuthorizedException, DatabaseManagerException {
        User user = getUserArg();
        if (user == null) throw new NonAuthorizedException();
        if(collectionManager.getCollection().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArg()||!hasWorkerArg()) throw new MissedCommandArgumentException();
        Long id = parseId(getStringArg());
        if(!databaseCollectionManager.checkWorkerByIdAndUserId(id, user)) throw new InvalidCommandArgumentException("no such id");
        WorkerFull oldWorker = databaseCollectionManager.getById(id);
        Worker worker = getWorkerArg();
        databaseCollectionManager.updateWorkerByID(id, worker);
        oldWorker.setName(worker.getName());
        oldWorker.setCoordinates(worker.getCoordinates());
        oldWorker.setSalary(worker.getSalary());
        oldWorker.setEndDate(worker.getEndDate());
        oldWorker.setPosition(worker.getPosition());
        oldWorker.setStatus(worker.getStatus());
        oldWorker.setPerson(worker.getPerson());
        boolean success = collectionManager.updateID(id, oldWorker);
        if (success) return "element #" + id + " updated";
        else throw new CommandException("cannot update");
    }
    
}
