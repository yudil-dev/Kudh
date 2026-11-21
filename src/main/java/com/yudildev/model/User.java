package com.yudildev.model;

import com.yudildev.exceptions.InvalidPasswordException;
import com.yudildev.exceptions.InvalidUsernameException;
import com.google.common.base.Preconditions;

import java.util.*;

public class User {

    private String username;
    private String password;

    private final Set<User> followedUsers;
    private final HashMap<Integer, Activity> historyActivity;
    private final Set<Coordinates> activitiesOccupiedCoords;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        historyActivity = new HashMap<>();
        activitiesOccupiedCoords =  new HashSet<>();
        followedUsers = new HashSet<>();
        checkUser();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }


    //Invariant checks
    private void checkUser() {

        checkUsername();
        checkPassword();
        checkHistoryActivity();
        checkOccCoords();
        checkFollowedUsers();
    }

    private void checkUsername() {
        Preconditions.checkNotNull(username, "Name can't be null");
        Preconditions.checkState(!username.isEmpty(), "Name can't be empty");
    }

    private void checkPassword() {
        Preconditions.checkNotNull(password, "Password can't be null");
        Preconditions.checkState(!password.isEmpty(), "Password can't be empty");
    }

    private void checkHistoryActivity() {
        Preconditions.checkNotNull(historyActivity, "Activity History can't be null");
        for (Activity activity : historyActivity.values()) {
            Preconditions.checkNotNull(activity, "Activity can't be null");
        }
    }

    private void checkFollowedUsers() {
        Preconditions.checkNotNull(followedUsers, "Followed Users can't be null");
        for (User user : followedUsers) {
            Preconditions.checkNotNull(user, "Followed User can't be null");
        }
    }

    private void checkOccCoords() {
        Preconditions.checkNotNull(activitiesOccupiedCoords, "Activities Occupied Coordinates can't be null");
        for (Coordinates coordinates : activitiesOccupiedCoords) {
            Preconditions.checkNotNull(coordinates, "Coordinates can't be null");
        }
    }


    //getters
    public Collection<Coordinates> getActivitiesOccupiedCoords() {
        return activitiesOccupiedCoords;
    }

    public HashMap<Integer, Activity> getHistoryActivity() {
        return historyActivity;
    }

    public Collection<User> getFollowedUsers() {
        return followedUsers;
    }

    public String getUsername() {
        return username;
    }


    //setters
    public void setUsername(String username) {
        this.username = username;
        checkUsername();
    }

    public void setPassword(String password) {
        this.password = password;
        checkPassword();
    }

    public void follow(User user) {
        Preconditions.checkNotNull(user, "Followed user can't be null");
        Preconditions.checkArgument(user != this, "User cannot follow themselves");
        followedUsers.add(user);
    }

    public void unfollow(User user) {
        followedUsers.remove(user);
        checkFollowedUsers();
    }

    public void addActivity(Activity activity) {
        Preconditions.checkNotNull(activity, "Activity to add can't be null");
        historyActivity.put(activity.getActivityID(), activity);
        activitiesOccupiedCoords.addAll(activity.getRoute());
        checkOccCoords();
    }

    public void removeActivity(Activity activity) {

        Preconditions.checkNotNull(activity, "Activity to remove can't be null");
        historyActivity.remove(activity.getActivityID());
        activitiesOccupiedCoords.clear();
        for (Activity a : historyActivity.values()) {
            activitiesOccupiedCoords.addAll(a.getRoute());
        }

    }


    public static class Builder {
        private String username;
        private String password;

        public Builder() {
            username = null;
            password = null;
        }

        public Builder username(String username) throws InvalidUsernameException {
            if (username != null && !username.isBlank()) {
                this.username = username;
            } else {
                throw new InvalidUsernameException();
            }

            return this;
        }

        public Builder password(String password) throws InvalidPasswordException {
            if (password != null && !password.isBlank()) {
                this.password = password;
            } else {
                throw new InvalidPasswordException();
            }
            return this;
        }

        public User build() {
            return new User(username, password);
        }
    }
}
