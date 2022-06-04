package common.io;

import common.data.*;
import common.exceptions.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Scanner;

import static common.io.OutputManager.print;

public class ConsoleInputManager extends InputManagerImpl{

    public ConsoleInputManager(){
        super(new Scanner(System.in));
        getScanner().useDelimiter("\n");
    }

    @Override
    public String readName() throws EmptyStringException {
        return new Question<String>("enter name:", super::readName).getAnswer();
    }

    @Override
    public String readPassword() throws EmptyStringException {
        return new Question<String>("enter password:", super::readPassword).getAnswer();
    }

    @Override
    public Integer readWeight() throws InvalidNumberException {
        return new Question<Integer>("enter weight:", super::readWeight).getAnswer();
    }

    @Override
    public LocalDateTime readBirthday() throws InvalidDateFormatException {
        return new Question<LocalDateTime>("enter birthday:", super::readBirthday).getAnswer();
    }

    @Override
    public double readXCoord() throws InvalidNumberException {
        return new Question<Double>("enter x:", super::readXCoord).getAnswer();
    }

    @Override
    public Integer readYCoord() throws InvalidNumberException {
        return new Question<Integer>("enter y:", super::readYCoord).getAnswer();
    }

    @Override
    public Coordinates readCoords() throws InvalidNumberException {
        print("enter coordinates");
        double x = readXCoord();
        Integer y = readYCoord();
        Coordinates coord = new Coordinates(x,y);
        return coord;
    }

    @Override
    public Long readSalary() throws InvalidNumberException {
        return new Question<Long>("enter salary:", super::readSalary).getAnswer();
    }

    @Override
    public ZonedDateTime readEndDate() throws InvalidDateFormatException {
        return new Question<ZonedDateTime>("enter endDate:", super::readEndDate).getAnswer();
    }

    @Override
    public Position readPosition() throws InvalidEnumException {
        return new Question<Position>("enter position(DIRECTOR, COOK, CLEANER):", super::readPosition).getAnswer();
    }

    @Override
    public Status readStatus() throws InvalidEnumException {
        return new Question<Status>("enter status(FIRED, HIRED, RECOMMENDED_FOR_PROMOTION, REGULAR):"
                , super::readStatus).getAnswer();
    }

    @Override
    public Color readColor() throws InvalidEnumException {
        return new Question<Color>("enter color(GREEN, RED, ORANGE):", super::readColor).getAnswer();
    }

    @Override
    public Person readPerson() throws InvalidDataException {
        print("enter person");
        Integer weight = readWeight();
        LocalDateTime birthday = readBirthday();
        Color eyeColor = readColor();
        return new Person(birthday, weight, eyeColor);
    }

    @Override
    public User readUser() throws InvalidDataException {
        print("enter user");
        String username = readName();
        String password = readPassword();
        return new User(username, password);
    }


}
