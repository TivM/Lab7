package serverModule.commands;

import serverModule.collection.CollectionManager;
import common.commands.CommandImpl;
import common.commands.CommandType;
import common.data.Worker;
import common.exceptions.FileException;
import common.file.ReaderWriter;

public class LoadCommand extends CommandImpl {
    ReaderWriter fileManager;
    CollectionManager collectionManager;
    public LoadCommand(CollectionManager cm, ReaderWriter fm){
        super("load",CommandType.SERVER_ONLY);
        collectionManager = cm;
        fileManager = fm;
    }
    @Override
    public String execute() throws FileException{
        if(hasStringArg()) {
            fileManager.setPath(getStringArg());
        };
        //collectionManager.deserializeCollection(fileManager.read());
        return "collection successfully loaded";
    }
}
