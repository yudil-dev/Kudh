package com.yudildev.model;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;

public class ExerciseTracker {

    private final Collection<User> users;
    private final Map world;

    public ExerciseTracker(Map world) {
        this.world = world;
        this.users = new ArrayList<>();
        checkExerciseTracker();
    }

    public void addUser(User user) {
        Preconditions.checkNotNull(user, "User cannot be null");
        users.add(user);
    }

    public Map getMap() {
        return world;
    }

    public Collection<User> getUsers() {
        return users;
    }


    //Invariant checks
    private void checkExerciseTracker() {
        Preconditions.checkNotNull(world, "Map can't be null");
        Preconditions.checkNotNull(users, "Users can't be null");
    }
}