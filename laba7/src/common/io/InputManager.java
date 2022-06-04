package common.io;

import common.connection.CommandMsg;
import common.data.*;
import common.exceptions.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Scanner;

public interface InputManager {

    /**
     * reads name from input
     * @return
     * @throws EmptyStringException
     */
    public String readName() throws EmptyStringException;

    /**
     * reads password from input
     * @return
     * @throws EmptyStringException
     * @throws InvalidDataException
     */
    public String readPassword() throws EmptyStringException, InvalidDataException;

    /**
     * reads weight from input
     * @return
     */
    public Integer readWeight() throws InvalidNumberException;

    /**
     * reads birthday from input
     * @return
     * @throws InvalidDateFormatException
     */
    public LocalDateTime readBirthday() throws InvalidDateFormatException;

    /**
     * reads x from input
     * @return
     * @throws InvalidNumberException
     */
    public double readXCoord() throws InvalidNumberException;

    /**
     * reads y from input
     * @return
     * @throws InvalidNumberException
     */
    public Integer readYCoord() throws InvalidNumberException;

    /**
     * reads coordinates from input
     * @return
     * @throws InvalidNumberException
     */
    public Coordinates readCoords() throws InvalidNumberException;

    /**
     * reads salary from input
     * @return
     * @throws InvalidNumberException
     */
    public Long readSalary() throws InvalidNumberException;

    /**
     * reads endDate from input
     * @return
     * @throws InvalidDateFormatException
     */
    public ZonedDateTime readEndDate() throws InvalidDateFormatException;

    /**
     * reads position from input
     * @return
     * @throws InvalidEnumException
     */
    public Position readPosition() throws InvalidEnumException;

    /**
     * reads status from input
     * @return
     * @throws InvalidEnumException
     */
    public Status readStatus() throws InvalidEnumException;

    /**
     * reads organizationType from input
     * @return
     * @throws InvalidEnumException
     */
    public Color readColor() throws InvalidEnumException;

    /**
     * reads organization from input
     * @return
     * @throws InvalidDataException
     */
    public Person readPerson() throws InvalidDataException;

    /**
     * reads User from input
     * @return
     * @throws InvalidDataException
     */
    public User readUser() throws InvalidDataException;

    /**
     * reads Worker from input
     * @return
     * @throws InvalidDataException
     */
    public Worker readWorker() throws InvalidDataException;

    /**
     * reads command-argument pair from input
     * @return
     */
    public CommandMsg readCommand();

    /**
     * gets input scanner
     * @return
     */
    public Scanner getScanner();
}
