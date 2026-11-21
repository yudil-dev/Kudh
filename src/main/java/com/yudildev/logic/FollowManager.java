package com.yudildev.logic;

import com.yudildev.exceptions.AlreadyFollowingUserException;
import com.yudildev.exceptions.CannotFollowSelfException;
import com.yudildev.exceptions.NoUserFoundException;
import com.yudildev.model.User;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class FollowManager {

    private final SessionManager sessionManager;

    public FollowManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void followUser(String username)
            throws
            NoUserFoundException,
            CannotFollowSelfException,
            AlreadyFollowingUserException {

        Preconditions.checkState(sessionManager.isLoggedIn(), "No user logged in");

        if (username == null || username.isBlank()) {
            throw new NoUserFoundException();
        }

        User currUser = sessionManager.getCurrUser();
        User userToFollow = findUserByUsername(username);

        if (userToFollow == null) {
            throw new NoUserFoundException();
        }

        if (currUser.equals(userToFollow)) {
            throw new CannotFollowSelfException();
        }

        if (currUser.getFollowedUsers().contains(userToFollow)) {
            throw new AlreadyFollowingUserException();
        }

        currUser.follow(userToFollow);

        Preconditions.checkState(currUser.getFollowedUsers().contains(userToFollow), "user follow wasn't sucessfull");
    }

    public List<User> getFollowableUsers() {
        User currUser = sessionManager.getCurrUser();
        List<User> followableUsers = new ArrayList<>();

        for (User user : sessionManager.getTracker().getUsers()) {
            if (!currUser.equals(user) && !currUser.getFollowedUsers().contains(user)) {
                followableUsers.add(user);
            }
        }
        return followableUsers;
    }

    private User findUserByUsername(String username) {
        User found = null;

        for (User user : sessionManager.getTracker().getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {
                found = user;
            }
        }

        return found;
    }
}