package serverModule.collection;
import common.data.Worker;
import common.data.WorkerFull;

import java.util.List;
import java.util.TreeSet;

/**
 * interface for storing elements
 */
public interface CollectionManager {
    /**
     * generates new unique ID for collection
     * @return
     */
    public Long generateNextId();

//    /**
//     * sorts collection
//     */
//    public void sort();

    public TreeSet<WorkerFull> getCollection();

    /**
     * adds new element
     * @param element
     */
    public void add(WorkerFull element);

    /**
     * get information about collection
     * @return info
     */
    public String getInfo();

    /**
     * checks if collection contains element with particular id
     * @param ID
     * @return
     */
    public boolean checkID(Long ID);

    /**
     * removes element by id
     * @param id
     */
    public boolean removeByID(Long id);

    /**
     * updates element by id
     * @param id
     * @param newElement
     * @return
     */
    public boolean updateID(Long id, WorkerFull newElement);

    /**
     * get collection size
     * @return
     */
    public int getSize();
   
    public void clear();



    /**
     * adds element if it is smaller than min
     * @param element
     */
    public boolean addIfMin(WorkerFull element);

    /**
     * print all elements which name starts with substring
     * @param salary
     */
    public  List<WorkerFull> printGreaterThanSalary(Long salary);


    /**
     * print the sum of the values of the salary field for all elements of the collection
     */
    public Long sumOfSalary();

    /**
     * print the average value of the salary field for all items in the collection
     */
    public Long averageOfSalary();

    /**
     * parse collection from json
     * @return
     */
    public String serializeCollection();
   
}
