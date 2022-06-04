package common.data;

import java.time.ZonedDateTime;

public class DefaultWorker extends Worker{
    public DefaultWorker(String name, Coordinates coordinates, Long salary, ZonedDateTime endDate, Position position, Status status, Person person){
        super(name, coordinates, salary, endDate, position, status, person);
        setCreationDate(ZonedDateTime.now());
    }
}
