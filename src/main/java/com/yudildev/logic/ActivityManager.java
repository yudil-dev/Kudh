package com.yudildev.logic;

import com.yudildev.exceptions.NoActivityFoundException;
import com.yudildev.model.Activity;
import com.yudildev.model.Obstacle;
import com.yudildev.model.User;
import com.google.common.base.Preconditions;

import java.util.Collection;

public class ActivityManager {

    private final SessionManager sessionManager;

    public ActivityManager(SessionManager sessionManager) {
        Preconditions.checkNotNull(sessionManager, "SessionManager cannot be null");
        this.sessionManager = sessionManager;
    }

    public void addCompletedActivity(Activity activity,
                                     Collection<Obstacle> encounteredObstacles) {

        Preconditions.checkNotNull(activity, "Activity cannot be null");
        Preconditions.checkState(sessionManager.isLoggedIn(), "Current user is not logged in");

        User currUser = sessionManager.getCurrUser();

        if (encounteredObstacles != null) {
            for (Obstacle obstacle : encounteredObstacles) {
                sessionManager.getTracker().getMap().addObstacle(obstacle);
            }
        }
        currUser.addActivity(activity);

        Preconditions.checkState(getCurrUserActivities().contains(activity), "Activity was not added");
    }

    public void removeActivity(Activity activity) {
        Preconditions.checkState(sessionManager.isLoggedIn(), "Current user is not logged in");
        Preconditions.checkNotNull(activity, "Activity cannot be null");

        User currUser = sessionManager.getCurrUser();
        currUser.removeActivity(activity);

        Preconditions.checkState(!getCurrUserActivities().contains(activity), "Activity was not removed");
    }

    public Collection<Activity> getCurrUserActivities() {
        Preconditions.checkState(sessionManager.getCurrUser() != null, "Current user is not logged in");
        return sessionManager.getCurrUser().getHistoryActivity().values();
    }

    public Activity getCurrUserActivityByID(int activityID)
            throws NoActivityFoundException {

        Preconditions.checkState(sessionManager.isLoggedIn(), "Current user is not logged in");

        Activity activity = sessionManager.getCurrUser().getHistoryActivity().get(activityID);

        if (activity == null) {
            throw new NoActivityFoundException();
        }

        return activity;
    }


}