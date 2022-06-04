package serverModule.commands;

import serverModule.collection.CollectionManager;
import serverModule.collection.DataHasher;
import serverModule.collection.DatabaseCollectionManager;
import serverModule.collection.DatabaseUserManager;
import common.commands.Command;
import common.commands.CommandManager;
import common.connection.AnswerMsg;
import common.connection.Request;
import common.exceptions.CommandException;
import common.file.ReaderWriter;
import serverModule.log.*;
import serverModule.server.*;

public class ServerCommandManager extends CommandManager{
    private Server server;
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;
    private DatabaseUserManager databaseUserManager;
    private ReaderWriter fileManager;

    public ServerCommandManager(Server  serv){
        server = serv;
        collectionManager = server.getCollectionManager();
        databaseCollectionManager = serv.getDatabaseCollectionManager();
        databaseUserManager = serv.getDatabaseUserManager();
        fileManager = server.getFileManager();
        addCommand(new ExitCommand());
        addCommand(new HelpCommand());
        addCommand(new SignInCommand(databaseUserManager));
        addCommand(new SignUpCommand(databaseUserManager));
        addCommand(new ExecuteScriptCommand(this));
        addCommand(new InfoCommand(collectionManager));
        addCommand(new AddCommand(collectionManager, databaseCollectionManager));
        addCommand(new SumOfSalaryCommand(collectionManager));
        addCommand(new AverageOfSalaryCommand(collectionManager));
        addCommand(new AddIfMinCommand(collectionManager, databaseCollectionManager));
        addCommand(new UpdateCommand(collectionManager, databaseCollectionManager));
        addCommand(new RemoveByIdCommand(collectionManager, databaseCollectionManager));
        addCommand(new ClearCommand(collectionManager, databaseCollectionManager));
        addCommand(new ShowCommand(collectionManager));
        addCommand(new FilterGreaterThanSalaryCommand(collectionManager));
        addCommand(new LoadCommand(collectionManager,fileManager));
        addCommand(new LogOutCommand(databaseUserManager));
        addCommand(new ExitUserCommand(databaseUserManager));
    }
    public Server getServer(){
        return server;
    }
    @Override
    public AnswerMsg runCommand(Request msg) {
        AnswerMsg res = new AnswerMsg();
        try {
            if (msg.getUser() != null) {
                msg.getUser().setPassword(DataHasher.hash(msg.getUser().getPassword() + "!Hq78p@T"));
            }
            Command cmd = getCommand(msg.getCommandName());
            cmd.setArgument(msg);
            res = (AnswerMsg) cmd.run();
        } catch (CommandException e){
            res.error(e.getMessage());
        }
        switch (res.getStatus()){
            case EXIT:
                Log.logger.fatal(res.getMessage());
                server.close();
                break;
            case ERROR:
                Log.logger.error(res.getMessage());
                break;
            default:
                Log.logger.info(res.getMessage());
                break;
        }
        return res;
    }
}
