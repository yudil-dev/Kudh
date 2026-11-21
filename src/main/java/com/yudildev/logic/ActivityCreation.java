package com.yudildev.logic;

import com.google.common.base.Preconditions;
import com.yudildev.exceptions.*;
import com.yudildev.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActivityCreation {

    private final ExerciseTracker tracker;

    public ActivityCreation(ExerciseTracker tracker) {

        Preconditions.checkNotNull(tracker, "Tracker cannot be null");

        this.tracker = tracker;
    }

    public Activity createActivity(ActivityType type,
                                   LocalDateTime startDateTime,
                                   LocalDateTime endDateTime,
                                   List<Coordinates> route)
            throws InvalidActivityTypeException,
            InvalidStartDateTimeException,
            InvalidEndDateTimeException,
            InvalidRouteException, CoordinateOutOfBoundsException,
            RouteCoordinateInObstacleException,
            DuplicateCoordinateException,
            DiscontinousRouteException {

        Preconditions.checkNotNull(type, "Type cannot be null");
        Preconditions.checkNotNull(startDateTime, "StartDateTime cannot be null");
        Preconditions.checkNotNull(endDateTime, "EndDateTime cannot be null");
        Preconditions.checkNotNull(route, "Route cannot be null");

        if (route.isEmpty()) {
            throw new InvalidRouteException();
        }
        List<Coordinates> checkedRoute = new ArrayList<>();

        for (Coordinates coordinate : route) {
            validateNextCoordinate(coordinate, checkedRoute);
            checkedRoute.add(coordinate);
        }

        return new Activity.Builder()
                .type(type)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .route(checkedRoute)
                .build();
    }

    public void validateNextCoordinate(Coordinates coordinate, List<Coordinates> currentRoute)
            throws CoordinateOutOfBoundsException,
            RouteCoordinateInObstacleException,
            DuplicateCoordinateException,
            DiscontinousRouteException {

        Preconditions.checkNotNull(coordinate, "Coordinate cannot be null");
        Preconditions.checkNotNull(currentRoute, "Route cannot be null");

        validateNextCoordinate(coordinate, currentRoute, null);
    }

    public void validateNextCoordinate(Coordinates coordinate,
                                       List<Coordinates> currentRoute,
                                       Collection<Obstacle> extraObstacles)
            throws CoordinateOutOfBoundsException,
            RouteCoordinateInObstacleException,
            DuplicateCoordinateException,
            DiscontinousRouteException {

        Preconditions.checkNotNull(coordinate, "Coordinate cannot be null");
        Preconditions.checkNotNull(currentRoute, "Route cannot be null");

        if (!tracker.getMap().coordWithinMap(coordinate)) {
            throw new CoordinateOutOfBoundsException();
        }

        if (tracker.getMap().coordInObstacle(coordinate)) {
            throw new RouteCoordinateInObstacleException();
        }

        if (extraObstacles != null) {
            for (Obstacle o : extraObstacles) {
                if (o.getLocation().contains(coordinate)) {
                    throw new RouteCoordinateInObstacleException();
                }
            }
        }

        if (currentRoute.contains(coordinate)) {
            throw new DuplicateCoordinateException();
        }

        if (!currentRoute.isEmpty()) {
            Coordinates prev = currentRoute.get(currentRoute.size() - 1);

            int dx = Math.abs(prev.getX() - coordinate.getX());
            int dy = Math.abs(prev.getY() - coordinate.getY());

            if (dx + dy != 1) {
                throw new DiscontinousRouteException();
            }
        }
    }
}