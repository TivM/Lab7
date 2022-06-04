package common.utils;

import common.exceptions.InvalidDateFormatException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides methods to convenient conversion between String and Date 
 */
public class DateConverter {
    private static  DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static  DateTimeFormatter zonedDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.of("UTC"));


    /**
     * convert ZonedDateTime to String
     * @param date
     * @return
     */
    public static String localDateTimeToString(LocalDateTime date){
        return date.format(localDateTimeFormatter);
    }

    /**
     * convert ZonedDateTime to String
     * @param s
     * @return
     * @throws InvalidDateFormatException
     */
    public static LocalDateTime parseLocalDateTime(String s) throws InvalidDateFormatException{
        try{
            return LocalDateTime.parse(s, localDateTimeFormatter);
        }
        catch(java.time.format.DateTimeParseException e){
            throw new InvalidDateFormatException();
        }
    }


    /**
     * convert ZonedDateTime to String
     * @param date
     * @return
     */
    public static String zonedDateTimeToString(ZonedDateTime date){
        return zonedDateFormatter.format(date);
    }

    /**
     * convert ZonedDateTime to String
     * @param s
     * @return
     * @throws InvalidDateFormatException
     */
    public static ZonedDateTime parseZonedDateTime(String s) throws InvalidDateFormatException {
        try {
            return ZonedDateTime.parse(s, zonedDateFormatter);
//            LocalDateTime tmp = LocalDateTime.parse (s, zonedDateFormatter);
//            return ZonedDateTime.of (tmp, ZoneId.of ("UTC"));
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
