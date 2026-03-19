package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Obstacle {

    private static int countObstacle = 0;

    private final int obstacleID;
    private final ArrayList<Coordinates> location;


    public Obstacle(ArrayList<Coordinates> location) {
        this.location = location;
        obstacleID = ++countObstacle;
        checkObstacleFinalization();
    }

    public Obstacle() {
        this.location = new ArrayList<>();
        obstacleID = ++countObstacle;
    }


    public void addCoord(Coordinates coordToAdd) {

        if (!location.contains(coordToAdd)) {
            location.add(coordToAdd);
        }

        checkObstacleLocation();
    }

    public void removeCoord(Coordinates coordToRemove) {
        location.remove(coordToRemove);
    }


    @Override
    public String toString() {
        return String.format("ObstacleID = %d\nLocation: %s", obstacleID, location);
    }


    //Getters
    public ArrayList<Coordinates> getLocation() {
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


    //Invariant Checks
    public void checkObstacleFinalization() {
        checkObstacleLocation();
        Preconditions.checkNotNull(location, "Location can't be null");
        Preconditions.checkState(!location.isEmpty(), "Location can't be empty");
    }

    public void checkObstacleLocation() {
        Preconditions.checkNotNull(location, "Location can't be null");
        for (var coordinate : location) {
            Preconditions.checkNotNull(coordinate, "Coordinates can't be null");
        }
    }

}
