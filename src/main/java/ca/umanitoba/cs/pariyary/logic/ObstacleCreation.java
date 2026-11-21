package ca.umanitoba.cs.pariyary.logic;

import ca.umanitoba.cs.pariyary.exceptions.*;
import ca.umanitoba.cs.pariyary.model.Coordinates;
import ca.umanitoba.cs.pariyary.model.ExerciseTracker;
import ca.umanitoba.cs.pariyary.model.Obstacle;
import ca.umanitoba.cs.pariyary.model.ObstacleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObstacleCreation {
    private final ExerciseTracker tracker;

    public ObstacleCreation(ExerciseTracker tracker) {
        this.tracker = tracker;
    }

    public Obstacle createObstacle (ObstacleType type, Collection<Coordinates> location)
        throws InvalidObstacleLocationException,
            EmptyObstacleLocationException,
            InvalidObstacleTypeException,
            CoordinateOutOfBoundsException,
            DuplicateCoordinateException,
            ObstacleCoordInPrevObstacle,
            InvalidObstacleShapeException{

        if (location.isEmpty()) {
            throw new EmptyObstacleLocationException();
        }

        List<Coordinates> checkedLocation = new ArrayList<>();

        for (Coordinates coordinate : location) {
            validateNextCoordinate(coordinate, checkedLocation);
            checkedLocation.add(coordinate);
        }

        Obstacle temp = new Obstacle.Builder()
                .type(type)
                .location(checkedLocation)
                .build();

        if (!tracker.getMap().isObsRectangle(temp)) {
            throw new InvalidObstacleShapeException();
        }

        return new Obstacle.Builder()
                .type(type)
                .location(checkedLocation)
                .build();
    }

    public void validateNextCoordinate(Coordinates coordinate,
                                       List<Coordinates> currentLocation)
            throws CoordinateOutOfBoundsException,
            DuplicateCoordinateException,
            ObstacleCoordInPrevObstacle{

        if (!tracker.getMap().coordWithinMap(coordinate)) {
            throw new CoordinateOutOfBoundsException();
        }

        if (tracker.getMap().coordInObstacle(coordinate)) {
            throw new ObstacleCoordInPrevObstacle();
        }

        if (currentLocation.contains(coordinate)) {
            throw new DuplicateCoordinateException();
        }
    }

}
