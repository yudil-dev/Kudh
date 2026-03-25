package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Obstacle {


    private static int countObstacle = 0;

    private final ObstacleType type;
    private final int obstacleID;
    private final List<Coordinates> location;


    public Obstacle(ObstacleType type, List<Coordinates> location) {
        this.type = type;
        this.location = location;
        obstacleID = ++countObstacle;
        checkObstacle();
    }

//    public Obstacle() {
//        this.location = new ArrayList<>();
//        obstacleID = ++countObstacle;
//    }

//
    public void addCoord(Coordinates coordToAdd) {

        if (!location.contains(coordToAdd)) {
            location.add(coordToAdd);
        }
        checkObstacle();

    }

    public void removeCoord(Coordinates coordToRemove) {
        location.remove(coordToRemove);
        checkObstacle();
    }


    @Override
    public String toString() {
        return String.format("[ObstacleID: %d, Type: %s, Location: %s]", obstacleID, type, location);
    }

    //Getters
    public  ObstacleType getType() {
        return type;
    }

    public List<Coordinates> getLocation() {
        return location;
    }

    public int getObstacleID() {
        return obstacleID;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (this == obj) {
            isEqual = true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Obstacle other = (Obstacle) obj;
            if (this.obstacleID == other.obstacleID) {
                isEqual = true;
            }
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return obstacleID;
    }

    //Invariant
    public void checkObstacle() {
        Preconditions.checkNotNull(location, "Location can't be null");
        Preconditions.checkState(!location.isEmpty(), "Location can't be empty");
        for (var coordinate : location) {
            Preconditions.checkNotNull(coordinate, "Coordinates can't be null");
        }
    }

}
