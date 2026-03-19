package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private final String name;
    private final HashMap<Integer, Activity> historyActivity;

    public User(String name) {
        this.name = name;
        historyActivity = new HashMap<>();
        checkUser();
    }


    public void addActivity(Activity activity) {
        historyActivity.put(activity.getActivityID(), activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            historyActivity.remove(activity.getActivityID());
        }
    }


    public HashMap<Integer, Activity> getHistoryActivity() {
        return historyActivity;
    }


    //Invariant checks
    private void checkUser() {

        Preconditions.checkNotNull(name, "Name can't be null");
        Preconditions.checkState(!name.isEmpty(), "Name can't be empty");

        checkHistoryActivity();
    }

    private void checkHistoryActivity() {
        Preconditions.checkNotNull(historyActivity, "Activity History can't be null");
        for (Activity activity : historyActivity.values()) {
            Preconditions.checkNotNull(activity, "Activity can't be null");
        }
    }

    public int getTotalSince(LocalDateTime cutoff) {
        int total = 0;

        for (Activity activity : historyActivity.values()) {
            if (!activity.getStartDateTime().isBefore(cutoff)) {
                total += Math.max(0, activity.getRoute().size() - 1);
            }
        }

        return total;
    }
}
