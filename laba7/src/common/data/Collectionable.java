package common.data;
/**
 * interface for storable object
 */
public interface Collectionable extends Comparable<Collectionable>, Validateable{

    public Long getId();
    /**
     * sets id, useful for replacing object in collection
     * @param ID
     */
    public void setId(Long ID);
    
    public Long getSalary();

    public String getName();

    /**
     * compairs two objects
     */
    public int compareTo(Collectionable worker);

    public boolean validate();
}
