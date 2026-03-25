package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

public class ExerciseTracker {

    private Map world;
    private final User user;

    public ExerciseTracker(User user, Map world) {
        this.user = user;
        this.world = world;

//      checkExerciseTracker();
    }

    public ExerciseTracker() {
        this.user = new User("Default");
        this.world = null;
    }

    public Map getMap() {
        return world;
    }

    public User getUser() {
        return user;
    }

    public void setWorld(Map world) {
        Preconditions.checkNotNull(world);
        this.world = world;
    }

    private void checkExerciseTracker() {
        Preconditions.checkNotNull(user, "User can't be null");
        Preconditions.checkNotNull(world, "Map can't be null");
    }

    //checks if activity's route is within map and not is not obstructing with obstacles
    private void checkActivity(Activity activity) {
        Preconditions.checkNotNull(activity, "Activity can't be null");
        Preconditions.checkState(isActivityRouteWithinMap(activity), "Activity Route not within Map's Bounds");
        Preconditions.checkState(isObstacleBlockActivityRoute(activity), "Activity Route goes through Obstacle");
    }

    private boolean isActivityRouteWithinMap(Activity activity) {
        for (Coordinates c : activity.getRoute()) {
            if (!world.coordWithinMap(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isObstacleBlockActivityRoute(Activity activity) {
        for (Coordinates c : activity.getRoute()) {
            if (world.getOccupiedCoords().contains(c)) {
                return false;
            }
        }
        return true;
    }

    public void addActivityToUser(Activity activity) {
        checkActivity(activity);
        user.addActivity(activity);
    }

    public void removeActivityFromUser(Activity activity) {
        checkActivity(activity);
        user.removeActivity(activity);
    }

    public void removeWorld() {
        this.world = null;
    }

}
