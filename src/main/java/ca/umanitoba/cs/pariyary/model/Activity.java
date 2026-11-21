package ca.umanitoba.cs.pariyary.model;

import ca.umanitoba.cs.pariyary.exceptions.*;
import com.google.common.base.Preconditions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Activity {

    private static int countActivity = 0;

    private final ActivityType type;
    private final LocalDateTime startDateTime, endDateTime;
    private final List<Coordinates> route;
    private final int activityID;

    public Activity(ActivityType type,
                    LocalDateTime startDateTime,
                    LocalDateTime endDateTime, List<Coordinates> route) {

        this.activityID = ++countActivity;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.route = route;

        checkActivity();
    }

    public void addCoordinates(Coordinates coordinates) {
        if (!route.contains(coordinates)) {
            route.add(coordinates);
        }
        checkRoute();
    }

    public void removeCoordinates(Coordinates coordinates) {
        route.remove(coordinates);
    }


    public int hashCode() {
        return activityID;
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (this == obj) {
            isEqual = true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Activity other = (Activity) obj;
            if (this.activityID == other.activityID) {
                isEqual = true;
            }
        }

        return isEqual;
    }


    public List<Coordinates> getRoute() {
        return new ArrayList<>(route);
    }

    public int getActivityID() {
        return activityID;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public ActivityType getType() {
        return type;
    }

    public String toString() {
        return String.format("[Activity ID: %d, Type: %s, StartDateTime: %s EndDateTime: %s Route: %s]",
                activityID,
                type,
                startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                route);
    }


    //Invariant Checks
    private void checkActivity() {
        Preconditions.checkNotNull(type, "activityType can't be null");

        Preconditions.checkNotNull(startDateTime, "startDateTime can't be null");
        Preconditions.checkNotNull(endDateTime, "endDateTime can't be null");
        Preconditions.checkState(startDateTime.isBefore(endDateTime),
                "StartDateTime must be before endDateTime ");

        Preconditions.checkNotNull(route, "route can't be null");
        Preconditions.checkState(!route.isEmpty(), "route can't be empty");
        checkRoute();
    }

    private void checkRoute() {
        for (var coordinates : route) {
            Preconditions.checkNotNull(coordinates, "coordinates can't be null");
        }
    }

    public static class Builder {
        private ActivityType type;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private final List<Coordinates> route;

        public Builder() {
            type = null;
            startDateTime = null;
            endDateTime = null;
            route = new ArrayList<>();
        }

        public Builder type(ActivityType type) throws InvalidActivityTypeException {
            if (type == null) {
                throw new InvalidActivityTypeException();
            }
            this.type = type;
            return this;
        }

        public Builder startDateTime(LocalDateTime startDateTime) throws InvalidStartDateTimeException {
            if (startDateTime == null) {
                throw new InvalidStartDateTimeException();
            }
            this.startDateTime = startDateTime;
            return this;
        }

        public Builder endDateTime(LocalDateTime endDateTime) throws InvalidEndDateTimeException {
            if (endDateTime == null || startDateTime == null || endDateTime.isBefore(startDateTime)) {
                throw new InvalidEndDateTimeException();
            }
            this.endDateTime = endDateTime;
            return this;
        }

        public Builder route(List<Coordinates> route) throws InvalidRouteException{

            if (route == null || route.isEmpty()) {
                throw new InvalidRouteException();
            }

            for (Coordinates c : route) {
                if (c == null) {
                    throw new InvalidRouteException();
                }
            }

            this.route.clear();
            this.route.addAll(route);
            return this;
        }

        public Activity build() {
            return new Activity(type, startDateTime, endDateTime, new ArrayList<>(route));
        }

    }

}
