package common.exceptions;

public class WrongAmountOfParametersException extends Exception{
    public WrongAmountOfParametersException() {
        super("wrong amount of parameters");
    }
}
