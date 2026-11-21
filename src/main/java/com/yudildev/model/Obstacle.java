package com.yudildev.model;

import com.yudildev.exceptions.InvalidObstacleLocationException;
import com.yudildev.exceptions.InvalidObstacleTypeException;
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
        this.location = new ArrayList<>(location);;
        obstacleID = ++countObstacle;
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
        return new ArrayList<>(location);
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
    private void checkObstacle() {
        Preconditions.checkNotNull(type, "Type cannot be null");
        Preconditions.checkNotNull(location, "Location can't be null");
        Preconditions.checkState(!location.isEmpty(), "Location can't be empty");
        for (var coordinate : location) {
            Preconditions.checkNotNull(coordinate, "Coordinates can't be null");
        }
    }

    public static class Builder {
        private ObstacleType type;
        private final List<Coordinates> location;

        public Builder() {
            type = null;
            location = new ArrayList<>();
        }

        public Builder type(ObstacleType type) throws InvalidObstacleTypeException {
            if (type == null) {
                throw new InvalidObstacleTypeException();
            }
            this.type = type;
            return this;
        }


        public Builder location(List<Coordinates> location) throws InvalidObstacleLocationException {
            if (location == null || location.isEmpty()) {
                throw new InvalidObstacleLocationException();
            }

            for (Coordinates c : location) {
                if (c == null) {
                    throw new InvalidObstacleLocationException();
                }
            }

            this.location.clear();
            this.location.addAll(location);
            return this;
        }

        public Obstacle build() {
            return new Obstacle(type, new ArrayList<>(location));
        }

    }

}
