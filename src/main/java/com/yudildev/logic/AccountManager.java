package com.yudildev.logic;

import com.yudildev.exceptions.*;
import com.yudildev.model.Activity;
import com.yudildev.model.User;
import com.google.common.base.Preconditions;

public class AccountManager {

    private final SessionManager sessionManager;


    public AccountManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void changeUsername(String newUsername)
            throws
            InvalidUsernameException,
            UserAlreadyExistsException {

        checkAccountManager();
        checkUserLoggedIn();
        Preconditions.checkNotNull(newUsername, "newUsername can't be null");

        if (newUsername.isBlank()) {
            throw new InvalidUsernameException();
        }

        User currUser = sessionManager.getCurrUser();

        for (User user : sessionManager.getTracker().getUsers()) {
            if (user.getUsername().equalsIgnoreCase(newUsername)
                    && !user.equals(currUser)) {
                throw new UserAlreadyExistsException();
            }
        }

        currUser.setUsername(newUsername);
    }

    public void changePassword(String newPassword)
            throws
            InvalidPasswordException {

        checkAccountManager();
        checkUserLoggedIn();
        Preconditions.checkNotNull(newPassword, "newUsername can't be null");

        if (newPassword.isBlank()) {
            throw new InvalidPasswordException();
        }

        User currUser = sessionManager.getCurrUser();
        currUser.setPassword(newPassword);
    }

    public void unfollowUser(String username)
            throws NoUserFoundException, InvalidUsernameException {

        checkAccountManager();
        checkUserLoggedIn();
        Preconditions.checkNotNull(username, "Username can't be null");

        if (username.isBlank()) {
            throw new InvalidUsernameException();
        }

        User currUser = sessionManager.getCurrUser();

        User target = null;
        for (User user : currUser.getFollowedUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                target = user;
            }
        }

        if (target == null) {
            throw new NoUserFoundException();
        }

        currUser.getFollowedUsers().remove(target);
    }

    public void removeActivity(int activityID)
            throws NoActivityFoundException {

        checkAccountManager();
        checkUserLoggedIn();

        User currUser = sessionManager.getCurrUser();
        Activity activity = currUser.getHistoryActivity().get(activityID);

        if (activity == null) {
            throw new NoActivityFoundException();
        }

        currUser.removeActivity(activity);
    }

    private void checkUserLoggedIn() {
        Preconditions.checkState(sessionManager.isLoggedIn(), "Current user is not logged in");
    }

    public void checkAccountManager() {
        Preconditions.checkNotNull(sessionManager, "SessionManager can't be null");
    }
}