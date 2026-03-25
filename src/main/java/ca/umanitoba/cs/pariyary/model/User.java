package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class User {

    private final String name;

    private final HashMap<Integer, Activity> historyActivity;
    private final Collection<Coordinates> activitiesOccupiedCoords;

    public User(String name) {
        this.name = name;
        historyActivity = new HashMap<>();
        activitiesOccupiedCoords =  new HashSet<>();
        checkUser();
    }


    public void addActivity(Activity activity) {
        historyActivity.put(activity.getActivityID(), activity);
        activitiesOccupiedCoords.addAll(activity.getRoute());
        checkHistoryActivity();
        checkOccCoords();
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            historyActivity.remove(activity.getActivityID());
            activitiesOccupiedCoords.removeAll(activity.getRoute());
        }
        checkHistoryActivity();
        checkOccCoords();
    }

    public Collection<Coordinates> getActivitiesOccupiedCoords() {
        return activitiesOccupiedCoords;
    }


    public HashMap<Integer, Activity> getHistoryActivity() {
        return historyActivity;
    }

    //Invariant checks
    private void checkUser() {

        Preconditions.checkNotNull(name, "Name can't be null");
        Preconditions.checkState(!name.isEmpty(), "Name can't be empty");

        checkHistoryActivity();
        checkOccCoords();
    }

    private void checkHistoryActivity() {
        Preconditions.checkNotNull(historyActivity, "Activity History can't be null");
        for (Activity activity : historyActivity.values()) {
            Preconditions.checkNotNull(activity, "Activity can't be null");
        }
    }

    private void checkOccCoords() {
        Preconditions.checkNotNull(activitiesOccupiedCoords, "Activities Occupied Coordinates can't be null");
        for (Coordinates coordinates : activitiesOccupiedCoords) {
            Preconditions.checkNotNull(coordinates, "Coordinates can't be null");
        }
    }
}
