package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class Activity {

    private static int countActivity = 0;

    private final ActivityType type;
    private final LocalDateTime startDateTime, endDateTime;
    private final ArrayList<Coordinates> route;
    private final int activityID;

    public Activity(ActivityType type,
                    LocalDateTime startDateTime,
                    LocalDateTime endDateTime) {

        this.activityID = ++countActivity;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.route = new ArrayList<>();

        //checkActivity();
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


    public ArrayList<Coordinates> getRoute() {
        return route;
    }

    int getActivityID() {
        return activityID;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }


    public String toString() {
        return String.format("""
                Activity ID = %d
                Type = %s
                StartDateTime = %s
                EndDateTime = %s
                Route = %s
                """,
                activityID,
                type,
                startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                route);
    }


    //Invariant Checks
    public void checkActivity() {
        Preconditions.checkNotNull(type, "activityType can't be null");

        Preconditions.checkNotNull(startDateTime, "startDateTime can't be null");
        Preconditions.checkNotNull(endDateTime, "endDateTime can't be null");
        Preconditions.checkState(startDateTime.isBefore(endDateTime),
                "StartDateTime cannot be less than or equal to endDateTime ");

        Preconditions.checkNotNull(route, "route can't be null");
        Preconditions.checkState(!route.isEmpty(), "route can't be empty");
        checkRoute();
    }

    private void checkRoute() {
        for (var coordinates : route) {
            Preconditions.checkNotNull(coordinates, "coordinates can't be null");
        }
    }

}
