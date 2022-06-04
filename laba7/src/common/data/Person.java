package common.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Organization class
 */
public class Person implements Validateable, Serializable {
    private LocalDateTime birthday; //Поле может быть null
    private Integer weight; //Поле может быть null, Значение поля должно быть больше 0
    private Color eyeColor; //Поле может быть null

    public Person(LocalDateTime birthday, Integer weight, Color eyeColor) {
        this.birthday = birthday;
        this.weight = weight;
        this.eyeColor = eyeColor;
    }

    /**
     * @return Color
     */
    public Color getEyeColor() {
        return eyeColor;
    }

    /**
     * @return Integer
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * @return LocalDateTime
     */
    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String bdayToString() {
        String strBirthday = "";
        if (birthday != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            strBirthday = birthday.format(formatter);
        }
        return strBirthday;
    }
    public String toString() {
        String s = "";
        s += "{";
        if (weight != null) s += "\"weight\" : " + "\"" +getWeight().toString() + "\"" + ", ";
        if (eyeColor!=null) s += "\"eyeColor\" : " + "\"" + getEyeColor().toString() + "\"" + ", ";
        if (birthday!=null) s += "\"birthday\" : " + "\"" + bdayToString() + "\"" + "}";
        return s;
    }
    public boolean validate(){
        return (
                (birthday==null || birthday!=null) && (weight==null || (weight!=null && weight>0)) &&
                        (eyeColor==null || eyeColor!=null)
        );
    }
}
