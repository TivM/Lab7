package common.data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

public class WorkerFull implements Collectionable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,
    // Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null,
    // Значение этого поля должно генерироваться автоматически
    private Long salary; //Поле может быть null, Значение поля должно быть больше 0
    private ZonedDateTime endDate; //Поле может быть null
    private Position position; //Поле не может быть null
    private Status status; //Поле не может быть null
    private Person person; //Поле может быть null
    private User user;

    public WorkerFull(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long salary, ZonedDateTime endDate, Position position, Status status, Person person, User user) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.endDate = endDate;
        this.position = position;
        this.status = status;
        this.person = person;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Long getSalary() {
        return salary;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Position getPosition() {
        return position;
    }

    public Status getStatus() {
        return status;
    }

    public Person getPerson() {
        return person;
    }

    public User getUser() {
        return user;
    }

    public int compareTo(Collectionable worker) {
        if (worker == null)
            return -1;
        if (worker.getSalary() == null)
            return -1;
        if (this.salary == null)
            return 1;
        int res = Long.compare(this.salary, worker.getSalary());
        return res;
    }

    @Override
    public boolean validate() {
        return (
                id!=null && coordinates!=null && coordinates.validate() &&
                        (user==null ||(!user.equals("") && user.validate())) &&
                        (person==null ||(!person.equals("") && person.validate())) &&
                        (salary==null ||(!salary.equals("") && salary>0)) && (id>0) &&
                        name!=null && !name.equals("") &&
                        status!=null &&
                        creationDate!=null &&
                        position!=null
        );
    }

    @Override
    public String toString(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String strCreationDate = dateFormatter.format(creationDate);
        String strEndDate = "";
        if (getEndDate()!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            strEndDate = getEndDate().format(formatter);
        }
        String s = "";
        s += "{\n";
        s += "  \"id\" : " + id + ",\n";
        s += "  \"name\" : " + "\"" + name + "\"" + ",\n";
        s += "  \"coordinates\" : " + coordinates.toString() + ",\n";
        s += "  \"creationDate\" : " + "\"" + strCreationDate + "\"" + ",\n";
        if (salary!=null) s += "  \"salary\" : " + Long.toString(salary) + ",\n";
        if (endDate!=null) s += "  \"endDate\" : " +  "\"" + strEndDate + "\"" + ",\n";
        s += "  \"position\" : " + "\"" + position.toString() + "\"" + ",\n";
        s += "  \"status\" : " + "\"" + status.toString() + "\"" + ",\n";
        s += "  \"person\" : " + person.toString() + "\n";
        s += "}";
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass()!= obj.getClass()) return false;
        WorkerFull another = (WorkerFull)obj;
        return this.getId().equals(another.getId());
    }

    public static class SortingComparator implements Comparator<WorkerFull> {
        public int compare(WorkerFull first, WorkerFull second) {
            int result = Double.compare(first.getCoordinates().getX(), second.getCoordinates().getX());
            if ( result == 0 ) {
                // both X are equal -> compare Y too
                result = Double.compare(first.getCoordinates().getY(), second.getCoordinates().getY());
            }
            return result;
        }
    }
}
