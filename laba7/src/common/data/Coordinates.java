package common.data;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Validateable, Serializable {
    private double x; //Максимальное значение поля: 850
    private Integer y; //Поле не может быть null
    public Coordinates(double x, Integer y){
        this.x = x;
        this.y = y;
    }

    /**
     * @return x coord
     */
    public double getX() {
        return x;
    }

    /**
     * @return y coord
     */
    public Integer getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString(){
        String s = "";
        s += "{\"x\" : " + Double.toString(x) + ", ";
        s += "\"y\" : " + Integer.toString(y) + "}";
        return s;
    }

    @Override
    public boolean validate(){
        return !(y==null || (Double)x > 850 || Double.isInfinite(x) || Double.isNaN(x));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
