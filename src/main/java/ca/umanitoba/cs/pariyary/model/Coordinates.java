package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;
import java.util.Objects;

public class Coordinates {

    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        checkCooridnates();
    }

    public Coordinates(String coordString) throws IllegalArgumentException {

        String[] tokens = coordString.split(" ");
        if  (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid format of coordinates");
        }
        final int INDEX_X = 0;
        final int INDEX_Y = 1;

        try {
            x = Integer.parseInt(tokens[INDEX_X]);
            y = Integer.parseInt(tokens[INDEX_Y]);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Only Integers are supported");
        }
        checkCooridnates();
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    //Invariant Checks
    public void checkCooridnates() {
        Preconditions.checkNotNull(x, "x can't be null");
        Preconditions.checkNotNull(y, "y can't be null");
    }


    @Override
    public boolean equals(Object o) {
        Coordinates other = (Coordinates) o;
        boolean isEqual = false;

        if (o != null && getClass() == o.getClass()) {
            other.checkCooridnates();
            isEqual = (x == other.x && y == other.y);
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
