package ca.umanitoba.cs.pariyary.model;

import com.google.common.base.Preconditions;

public class ExerciseTracker {

    private Map world;
    private User user;

    public ExerciseTracker(User user, Map world) {
        this.user = user;
        this.world = world;

        checkExerciseTracker();
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
        world.checkMap();
        this.world = world;
    }

    private void checkExerciseTracker() {
        Preconditions.checkNotNull(user, "User can't be null");
        Preconditions.checkNotNull(world, "Map can't be null");
    }

    //checks if activity's route is within map and not is not obstructing with obstacles
    private void checkActivity(Activity activity) {
        Preconditions.checkNotNull(activity, "Activity can't be null");
        Preconditions.checkState(world.isRouteWithinMap(activity.getRoute()), "Activity Route not within Map's Bounds");
        Preconditions.checkState(world.isObstacleBlockRoute(activity.getRoute()), "Activity Route goes through Obstacle");
    }

    public void addActivityToUserAndPlane(Activity activity) {
        checkActivity(activity);
        user.addActivity(activity);
        world.addRouteToPlane(activity.getRoute());
    }

    public void removeActivityFromUserAndPlane(Activity activity) {
        checkActivity(activity);
        user.removeActivity(activity);
        world.removeRouteFromPlane(activity.getRoute());
    }

    public void removeWorld() {
        this.world = null;
    }


}
