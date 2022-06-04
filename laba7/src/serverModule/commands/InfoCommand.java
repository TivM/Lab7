package serverModule.commands;

import serverModule.collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.data.WorkerFull;
import common.exceptions.InvalidDataException;

public class InfoCommand extends CommandImpl{
    private CollectionManager collectionManager;
    public InfoCommand(CollectionManager cm){
        super("info",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute() throws InvalidDataException {
        return collectionManager.getInfo();
    }

}
