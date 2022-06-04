package serverModule.commands;

import serverModule.collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.EmptyCollectionException;
public class ShowCommand extends CommandImpl{
    private CollectionManager collectionManager;
    public ShowCommand(CollectionManager cm){
        super("show",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if (collectionManager.getCollection().isEmpty()) return ("collection is empty");
        else return (collectionManager.serializeCollection());
    }
}
