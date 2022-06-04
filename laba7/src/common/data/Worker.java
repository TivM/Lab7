package common.data;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Worker class
 */
public class Worker implements Collectionable,Serializable{
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



    /**
     * constructor, just set fields
     * @param name
     * @param coordinates
     * @param salary
     * @param endDate
     * @param position
     * @param status
     * @param person
     */
    public Worker(String name, Coordinates coordinates, Long salary, ZonedDateTime endDate, Position position, Status status, Person person){
        this.creationDate = ZonedDateTime.now();
        this.name = name;
        this.coordinates = coordinates;
        this.salary = salary;
        this.endDate = endDate;
        this.position = position;
        this.status = status;
        this.person = person;
    }
    
    /** 
     * @return int
     */
    public Long getId(){
        return id;
    }

    /** 
     * sets ID, usefull for replacing elements in collection
     * @param ID
     */
    public void setId(Long ID){
        id = ID;
    }

    public void setCreationDate(ZonedDateTime date){
        creationDate = date;
    }
    public ZonedDateTime getCreationDate(){
        return creationDate;
    }
    /** 
     * @return String
     */
    public String getName(){
            return name;
    }

    public void setName(String s){
        name = s;
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

    /**
     * @return long
     */
    public Long getSalary(){
        return salary;
    }

    public void setSalary(long s){
        salary = s;
    }

    public void setStatus(Status s){
        status=s;
    }
    public Coordinates getCoordinates(){
        return coordinates;
    }

    
    /** 
     * @return LocalDate
     */
    public ZonedDateTime getEndDate(){
        return endDate;
    }

    public void setEndDate(ZonedDateTime date){
        endDate=date;
    }
    
    /** 
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass()!= obj.getClass()) return false;
        Worker another = (Worker)obj;
        return this.getId() == another.getId();
    }

    
    /** 
     * @param worker
     * @return int
     */
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

    
    /** 
     * @return boolean
     */
    public boolean validate(){
        return (
                coordinates!=null && coordinates.validate() &&
                        (person==null ||(!person.equals("") && person.validate())) &&
                        (salary==null ||(!salary.equals("") && salary>0)) && (id>0) &&
                        name!=null && !name.equals("") &&
                        status!=null &&
                        creationDate!=null &&
                        position!=null
        );

    }
}
