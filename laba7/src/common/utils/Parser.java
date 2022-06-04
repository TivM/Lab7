package common.utils;

import common.exceptions.InvalidNumberException;

public class Parser {
    public static Long parseId(String  s) throws InvalidNumberException{
        try{
            return Long.parseLong(s);
        }
        catch(NumberFormatException e){
            throw new InvalidNumberException();
        }
    }
}
