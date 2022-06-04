package serverModule.collection;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.data.WorkerFull;
import common.exceptions.DatabaseManagerException;
import serverModule.json.*;
import serverModule.log.Log;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Operates collection.
 */
public class WorkerCollectionManager implements CollectionManager {
    private TreeSet<WorkerFull> collection;
    private ZonedDateTime initDate;
    private HashSet<Long> uniqueIds;
    private DatabaseCollectionManager databaseCollectionManager;
    private ReentrantLock locker = new ReentrantLock();

    /**
     * Constructor, set start values
     */
    public WorkerCollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        uniqueIds = new HashSet<>();
        collection = new TreeSet<>();
        initDate = ZonedDateTime.now();
        this.databaseCollectionManager = databaseCollectionManager;
        loadCollection();
    }

    public void loadCollection() {
        try {
            collection = databaseCollectionManager.getCollection();
        } catch (DatabaseManagerException e) {
            Log.logger.error(e.getMessage());
        }
    }

    public Long generateNextId() {
        if (collection.isEmpty())
            return 1L;
        else {
            Long id = collection.last().getId() + 1;
            if (uniqueIds.contains(id)) {
                while (uniqueIds.contains(id)) id += 1;
            }
            uniqueIds.add(id);
            return id;
        }
    }

    /**
     * Return collection
     *
     * @return Collection
     */
    public TreeSet<WorkerFull> getCollection() {
        return collection;
    }

    /**
     * Add element to collection
     * -
     *
     * @param worker Element of collection
     */
    public void add(WorkerFull worker) {
        locker.lock();
        try {
            collection.add(worker);
        } finally {
            locker.unlock();
        }
    }

    /**
     * Get information about collection
     *
     * @return Information
     */
    public String getInfo() {
        locker.lock();
        try {
            return "TreeSet of Worker, size: " + Integer.toString(collection.size()) + ", initialization date: " + initDate.toString();
        } finally {
            locker.unlock();
        }
    }


    /**
     * Give info about is this ID used
     *
     * @param ID
     * @return is it used or not
     */
    public boolean checkID(Long ID) {
        if (uniqueIds.contains(ID)) return true;
        return false;
    }

    /**
     * Delete element by ID
     *
     * @param id ID
     */
    public boolean removeByID(Long id) {
        locker.lock();
        try {
            Iterator<WorkerFull> iterator = collection.iterator();
            while (iterator.hasNext()) {
                WorkerFull workerFull = iterator.next();
                if (workerFull.getId().equals(id)) {
                    iterator.remove();
                    return true;
                }
            }
            return false;
        } finally {
            locker.unlock();
        }
    }

    /**
     * Update element by ID
     *
     * @param id ID
     * @return
     */
    public boolean updateID(Long id, WorkerFull newWorker) {
        locker.lock();
        try {
            Optional<WorkerFull> worker = collection.stream()
                    .filter(w -> w.getId().equals(id))
                    .findFirst();
            if (worker.isPresent()) {
                collection.remove(worker.get());
                newWorker.setId(id);
                collection.add(newWorker);
                return true;
            }
            return false;
        } finally {
            locker.unlock();
        }
    }

    /**
     * Get size of collection
     *
     * @return Size of collection
     */
    public int getSize() {
        locker.lock();
        try {
            return collection.size();
        } finally {
            locker.unlock();
        }
    }

    /**
     * clear collection
     */
    public void clear() {
        locker.lock();
        try {
            collection.clear();
            uniqueIds.clear();
        } finally {
            locker.unlock();
        }
    }


    /**
     * Add if ID of element smaller than min in collection
     *
     * @param worker Element
     */
    public boolean addIfMin(WorkerFull worker) {
        locker.lock();
        try {
            if (collection.stream()
                    .min(WorkerFull::compareTo)
                    .filter(w -> w.compareTo(worker) == -1)
                    .isPresent()) {
                return false;
            }
            return true;
        } finally {
            locker.unlock();
        }
    }

    /**
     * output elements whose salary field value is greater than the specified one
     *
     * @param salary
     */
    @Override
    public List<WorkerFull> printGreaterThanSalary(Long salary) {
        locker.lock();
        try {
            List<WorkerFull> list = new LinkedList<>();
            for (WorkerFull worker : collection) {
                if (worker.getSalary() == null) {
                    continue;
                } else if (worker.getSalary() > salary) {
                    list.add(worker);

                } else
                    continue;

            }
            return list;
        } finally {
            locker.unlock();
        }
    }

    /**
     * print the sum of the values of the salary field for all elements of the collection
     *
     * @return
     */
    @Override
    public Long sumOfSalary() {
        locker.lock();
        try {
            Long sum = 0L;
            for (WorkerFull worker : collection) {
                if (worker.getSalary() != null)
                    sum += worker.getSalary();
                else
                    continue;

            }
            return sum;
        } finally {
            locker.unlock();
        }
    }

    /**
     * print the average value of the salary field for all items in the collection
     *
     * @return
     */
    @Override
    public Long averageOfSalary() {
        locker.lock();
        try {
            long averageSalary = 0L;
            averageSalary = sumOfSalary() / (long) getSize();
            return averageSalary;
        } finally {
            locker.unlock();
        }
    }

    public String serializeCollection() {
        if (collection == null || collection.isEmpty()) return "";
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .setPrettyPrinting().create();
        String json = gson.toJson(collection);
        return json;
    }
}