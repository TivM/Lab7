package serverModule.collection;

import common.data.*;
import common.exceptions.DatabaseManagerException;
import serverModule.log.Log;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TreeSet;

public class DatabaseCollectionManager {
    private final String SELECT_ALL_WORKER = "SELECT * FROM " + DatabaseManager.WORKER_TABLE;
    private final String SELECT_WORKER_BY_ID = SELECT_ALL_WORKER + " WHERE " +
            DatabaseManager.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_WORKER_BY_ID_AND_USER_ID = SELECT_WORKER_BY_ID + " AND " +
            DatabaseManager.WORKER_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_WORKER = "INSERT INTO " +
            DatabaseManager.WORKER_TABLE + " (" +
            DatabaseManager.WORKER_TABLE_NAME_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_COORDINATES_ID_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_SALARY_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_END_DATE_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_POSITION_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_STATUS_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_PERSON_ID_COLUMN + ", " +
            DatabaseManager.WORKER_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_WORKER_BY_ID = "DELETE FROM " + DatabaseManager.WORKER_TABLE +
            " WHERE " + DatabaseManager.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_NAME_BY_ID = "UPDATE " + DatabaseManager.WORKER_TABLE + " SET " +
            DatabaseManager.WORKER_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_SALARY_BY_ID = "UPDATE " + DatabaseManager.WORKER_TABLE + " SET " +
            DatabaseManager.WORKER_TABLE_SALARY_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_POSITION_BY_ID = "UPDATE " + DatabaseManager.WORKER_TABLE + " SET " +
            DatabaseManager.WORKER_TABLE_POSITION_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_MARINE_STATUS_BY_ID = "UPDATE " + DatabaseManager.WORKER_TABLE + " SET " +
            DatabaseManager.WORKER_TABLE_STATUS_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.WORKER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseManager.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_ID = SELECT_ALL_COORDINATES + " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " =?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseManager.COORDINATES_TABLE + " (" +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?)";
    private final String UPDATE_COORDINATES_BY_ID = "UPDATE " + DatabaseManager.COORDINATES_TABLE + " SET " +
            DatabaseManager.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseManager.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_ID = "DELETE FROM " + DatabaseManager.COORDINATES_TABLE +
            " WHERE " + DatabaseManager.COORDINATES_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_ALL_PERSONS = "SELECT * FROM " + DatabaseManager.PERSON_TABLE;
    private final String SELECT_PERSON_BY_ID = SELECT_ALL_PERSONS + " WHERE " + DatabaseManager.PERSON_TABLE_ID_COLUMN + " =?";
    private final String INSERT_PERSON = "INSERT INTO " +
            DatabaseManager.PERSON_TABLE + " (" +
            DatabaseManager.PERSON_TABLE_BIRTHDAY_COLUMN + ", " +
            DatabaseManager.PERSON_TABLE_WEIGHT_COLUMN + ", " +
            DatabaseManager.PERSON_TABLE_EYE_COLOR_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_PERSON_BY_ID = "UPDATE " + DatabaseManager.PERSON_TABLE + " SET " +
            DatabaseManager.PERSON_TABLE_BIRTHDAY_COLUMN + " = ?, " +
            DatabaseManager.PERSON_TABLE_WEIGHT_COLUMN + " = ?, " +
            DatabaseManager.PERSON_TABLE_EYE_COLOR_COLUMN + " = ?" + " WHERE " +
            DatabaseManager.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_PERSON_BY_ID = "DELETE FROM " + DatabaseManager.PERSON_TABLE +
            " WHERE " + DatabaseManager.PERSON_TABLE_ID_COLUMN + " = ?";

    private DatabaseManager databaseManager;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseManager databaseManager, DatabaseUserManager databaseUserManager) {
        this.databaseManager = databaseManager;
        this.databaseUserManager = databaseUserManager;
    }

    private WorkerFull returnWorker(ResultSet resultSet, long id) throws SQLException{
        String name = resultSet.getString(DatabaseManager.WORKER_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByID(resultSet.getInt(DatabaseManager.WORKER_TABLE_COORDINATES_ID_COLUMN));
        ZonedDateTime creationDate = resultSet.getTimestamp(DatabaseManager.WORKER_TABLE_CREATION_DATE_COLUMN).toLocalDateTime().atZone(ZoneId.of("Europe/Moscow"));
        Long salary = resultSet.getLong(DatabaseManager.WORKER_TABLE_SALARY_COLUMN);
        if (resultSet.wasNull()) salary = null;
        ZonedDateTime endDate = resultSet.getTimestamp(DatabaseManager.WORKER_TABLE_END_DATE_COLUMN).toLocalDateTime().atZone(ZoneId.of("Europe/Moscow"));
        Position position = Position.valueOf(resultSet.getString(DatabaseManager.WORKER_TABLE_POSITION_COLUMN));
        Status status = Status.valueOf(resultSet.getString(DatabaseManager.WORKER_TABLE_STATUS_COLUMN));
        Person person = getPersonByID(resultSet.getInt(DatabaseManager.WORKER_TABLE_PERSON_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getInt(DatabaseManager.WORKER_TABLE_USER_ID_COLUMN));
        return new WorkerFull(id, name, coordinates, creationDate, salary, endDate, position, status, person, owner );
    }

    private Coordinates getCoordinatesByID(long id) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_COORDINATES_BY_ID, false);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinates = new Coordinates(
                        resultSet.getDouble(DatabaseManager.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getInt(DatabaseManager.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            Log.logger.trace("An error occurred when executing the SELECT_COORDINATES_BY_ID request");
            e.printStackTrace();
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinates;
    }

    private Person getPersonByID(long id) throws SQLException {
        Person person;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_PERSON_BY_ID, false);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer weight = resultSet.getInt(DatabaseManager.PERSON_TABLE_WEIGHT_COLUMN);
                if (resultSet.wasNull()) weight = null;
                String color = resultSet.getString(DatabaseManager.PERSON_TABLE_EYE_COLOR_COLUMN);
                person = new Person(
                        resultSet.getTimestamp(DatabaseManager.PERSON_TABLE_BIRTHDAY_COLUMN).toLocalDateTime(),
                        weight,
                        color == null ? null : Color.valueOf(color)
                );
            } else throw new SQLException();
        } catch (SQLException e) {
            Log.logger.trace("An error occurred when executing the SELECT_CHAPTER_BY_ID request");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return person;
    }

    public TreeSet<WorkerFull> getCollection() throws DatabaseManagerException {
        TreeSet<WorkerFull> workers = new TreeSet<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_ALL_WORKER, false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong(DatabaseManager.WORKER_TABLE_ID_COLUMN);
                workers.add(returnWorker(resultSet, id));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            Log.logger.trace("An error occurred while retrieving data from the database");
            throw new DatabaseManagerException();
        }
        return workers;
    }

    public WorkerFull insertWorker(Worker worker, User user) throws DatabaseManagerException {
        WorkerFull workerToInsert;
        PreparedStatement insertWorker = null;
        PreparedStatement insertCoordinates = null;
        PreparedStatement insertPerson = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            ZonedDateTime localDateTime = ZonedDateTime.now();

            insertPerson = databaseManager.doPreparedStatement(INSERT_PERSON, true);
            insertPerson.setTimestamp(1, worker.getPerson().getBirthday() != null ? Timestamp.valueOf(worker.getPerson().getBirthday()) : Timestamp.valueOf(LocalDateTime.now()));
            if (worker.getPerson().getWeight() == null) {
                insertPerson.setNull(2, Types.INTEGER);
            } else insertPerson.setInt(2, worker.getPerson().getWeight());
            insertPerson.setString(3, worker.getPerson().getEyeColor() != null ? worker.getPerson().getEyeColor().toString() : null);
            if (insertPerson.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetPerson = insertPerson.getGeneratedKeys();
            int personID;
            if (resultSetPerson.next()) personID = resultSetPerson.getInt(1);
            else throw new SQLException();

            insertCoordinates = databaseManager.doPreparedStatement(INSERT_COORDINATES, true);
            insertCoordinates.setDouble(1, worker.getCoordinates().getX());
            insertCoordinates.setInt(2, worker.getCoordinates().getY());
            if (insertCoordinates.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetCoordinates = insertCoordinates.getGeneratedKeys();
            int coordinatesID;
            if (resultSetCoordinates.next()) coordinatesID = resultSetCoordinates.getInt(1);
            else throw new SQLException();

            insertWorker = databaseManager.doPreparedStatement(INSERT_WORKER, true);
            insertWorker.setString(1, worker.getName());
            insertWorker.setInt(2, coordinatesID);
            insertWorker.setTimestamp(3, Timestamp.valueOf(localDateTime.toLocalDateTime()));
            if (worker.getSalary() == null) {
                insertWorker.setNull(4, Types.BIGINT);
            } else insertWorker.setLong(4, worker.getSalary());
            insertWorker.setTimestamp(5, Timestamp.valueOf(localDateTime.toLocalDateTime()));
            insertWorker.setString(6, worker.getPosition().toString());
            insertWorker.setString(7, worker.getStatus().toString());
            insertWorker.setInt(8, personID);
            insertWorker.setInt(9, databaseUserManager.getUserIdByUsername(user));
            if (insertWorker.executeUpdate() == 0) throw new SQLException();
            ResultSet resultSetWorker = insertWorker.getGeneratedKeys();
            long workerID;
            if (resultSetWorker.next()) workerID = resultSetWorker.getInt(1);
            else throw new SQLException();
            workerToInsert = new WorkerFull(
                    workerID,
                    worker.getName(),
                    worker.getCoordinates(),
                    localDateTime,
                    worker.getSalary(),
                    localDateTime,
                    worker.getPosition(),
                    worker.getStatus(),
                    worker.getPerson(),
                    user
            );
            databaseManager.commit();
            return workerToInsert;
        } catch (SQLException exception) {
            exception.printStackTrace();
            Log.logger.trace("An error occurred when adding a new object to the database");
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(insertPerson);
            databaseManager.closePreparedStatement(insertCoordinates);
            databaseManager.closePreparedStatement(insertWorker);
            databaseManager.setAutoCommit();
        }
    }

    public void deleteWorkerById(long workerID) throws DatabaseManagerException {
        PreparedStatement deleteWorker = null;
        PreparedStatement deleteCoordinates = null;
        PreparedStatement deletePerson = null;
        try {
            int coordinatesID = getCoordinatesIdByWorkerID(workerID);
            int personID = getPersonIdByWorkerID(workerID);
            deleteWorker = databaseManager.doPreparedStatement(DELETE_WORKER_BY_ID, false);
            deleteWorker.setLong(1, workerID);
            if (deleteWorker.executeUpdate() == 0) throw new SQLException();
            deleteCoordinates = databaseManager.doPreparedStatement(DELETE_COORDINATES_BY_ID, false);
            deleteCoordinates.setInt(1, coordinatesID);
            if (deleteCoordinates.executeUpdate() == 0) throw new SQLException();
            deletePerson = databaseManager.doPreparedStatement(DELETE_PERSON_BY_ID, false);
            deletePerson.setInt(1, personID);
            if (deletePerson.executeUpdate() == 0) throw new SQLException();
        } catch (SQLException exception) {
            Log.logger.trace("An error occurred when executing the DELETE_MARINE_BY_ID request");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(deleteWorker);
            databaseManager.closePreparedStatement(deleteCoordinates);
            databaseManager.closePreparedStatement(deletePerson);
        }
    }

    public void updateWorkerByID(Long workerID, Worker worker) throws DatabaseManagerException {
        PreparedStatement updateWorkerName = null;
        PreparedStatement updateWorkerCoordinates = null;
        PreparedStatement updateWorkerSalary = null;
        PreparedStatement updateWorkerPosition = null;
        PreparedStatement updateWorkerStatus = null;
        PreparedStatement updateWorkerPerson = null;
        try {
            databaseManager.setCommit();
            databaseManager.setSavepoint();

            updateWorkerName = databaseManager.doPreparedStatement(UPDATE_MARINE_NAME_BY_ID, false);
            updateWorkerCoordinates = databaseManager.doPreparedStatement(UPDATE_COORDINATES_BY_ID, false);
            updateWorkerSalary = databaseManager.doPreparedStatement(UPDATE_MARINE_SALARY_BY_ID, false);
            updateWorkerPosition = databaseManager.doPreparedStatement(UPDATE_MARINE_POSITION_BY_ID, false);
            updateWorkerStatus = databaseManager.doPreparedStatement(UPDATE_MARINE_STATUS_BY_ID, false);
            updateWorkerPerson = databaseManager.doPreparedStatement(UPDATE_PERSON_BY_ID, false);

            updateWorkerName.setString(1, worker.getName());
            updateWorkerName.setLong(2, workerID);
            if (updateWorkerName.executeUpdate() == 0) throw new SQLException();

            updateWorkerCoordinates.setDouble(1, worker.getCoordinates().getX());
            updateWorkerCoordinates.setInt(2, worker.getCoordinates().getY());
            updateWorkerCoordinates.setLong(3, getCoordinatesIdByWorkerID(workerID));
            if (updateWorkerCoordinates.executeUpdate() == 0) throw new SQLException();


            if (worker.getSalary() == null) {
                updateWorkerSalary.setNull(1, Types.BIGINT);
            } else updateWorkerSalary.setLong(1, worker.getSalary());
            updateWorkerSalary.setLong(2, workerID);
            if (updateWorkerSalary.executeUpdate() == 0) throw new SQLException();

            updateWorkerPosition.setString(1, worker.getPosition().toString());
            updateWorkerPosition.setLong(2, workerID);
            if (updateWorkerPosition.executeUpdate() == 0) throw new SQLException();

            updateWorkerStatus.setString(1, worker.getStatus().toString());
            updateWorkerStatus.setLong(2, workerID);
            if (updateWorkerStatus.executeUpdate() == 0) throw new SQLException();

            updateWorkerPerson.setTimestamp(1, worker.getPerson().getBirthday() != null ? Timestamp.valueOf(worker.getPerson().getBirthday()) : Timestamp.valueOf(LocalDateTime.now()));
            if (worker.getPerson().getWeight() == null) {
                updateWorkerPerson.setNull(2, Types.INTEGER);
            } else updateWorkerPerson.setInt(2, worker.getPerson().getWeight());
            updateWorkerPerson.setString(3, worker.getPerson().getEyeColor() != null ? worker.getPerson().getEyeColor().toString() : null);
            updateWorkerPerson.setInt(4, getPersonIdByWorkerID(workerID));
            if (updateWorkerPerson.executeUpdate() == 0) throw new SQLException();

            databaseManager.commit();
        } catch (SQLException exception) {
            Log.logger.trace("An error occurred while executing a group of object update requests");
            databaseManager.rollback();
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(updateWorkerName);
            databaseManager.closePreparedStatement(updateWorkerCoordinates);
            databaseManager.closePreparedStatement(updateWorkerSalary);
            databaseManager.closePreparedStatement(updateWorkerPosition);
            databaseManager.closePreparedStatement(updateWorkerStatus);
            databaseManager.closePreparedStatement(updateWorkerPerson);
            databaseManager.setAutoCommit();
        }
    }

    private int getPersonIdByWorkerID(Long workerID) throws SQLException {
        int personID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_WORKER_BY_ID, false);
            preparedStatement.setLong(1, workerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                personID = resultSet.getInt(DatabaseManager.WORKER_TABLE_PERSON_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            Log.logger.trace("An error occurred when executing the SELECT_SPACE_MARINE_BY_ID request");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return personID;
    }

    private int getCoordinatesIdByWorkerID(Long workerID) throws SQLException {
        int coordinatesID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_WORKER_BY_ID, false);
            preparedStatement.setLong(1, workerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                coordinatesID = resultSet.getInt(DatabaseManager.WORKER_TABLE_COORDINATES_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException e) {
            Log.logger.trace("An error occurred when executing the SELECT_SPACE_MARINE_BY_ID request");
            throw new SQLException(e);
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return coordinatesID;
    }

    public WorkerFull getById(Long workerID) throws DatabaseManagerException{
        WorkerFull workerFull = null;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_WORKER_BY_ID, false);
            preparedStatement.setLong(1, workerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workerFull = returnWorker(resultSet, workerID);
            } else throw new SQLException();
        } catch (SQLException exception) {
            Log.logger.trace("An error occurred when executing the SELECT_WORKER_BY_ID request");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
        return workerFull;
    }

    public boolean checkWorkerByIdAndUserId(Long workerID, User user) throws DatabaseManagerException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseManager.doPreparedStatement(SELECT_WORKER_BY_ID_AND_USER_ID, false);
            preparedStatement.setLong(1, workerID);
            preparedStatement.setInt(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            Log.logger.trace("An error occurred when executing the SELECT_WORKER_BY_ID_AND_USER_ID request");
            throw new DatabaseManagerException();
        } finally {
            databaseManager.closePreparedStatement(preparedStatement);
        }
    }

    public void clearCollection() throws DatabaseManagerException{
        TreeSet<WorkerFull> workers = getCollection();
        for (WorkerFull worker : workers) {
            deleteWorkerById(worker.getId());
        }
    }
}
