package common.exceptions;

public class DatabaseManagerException extends Exception{
    public DatabaseManagerException() {
        super("error accessing the database");
    }
}
