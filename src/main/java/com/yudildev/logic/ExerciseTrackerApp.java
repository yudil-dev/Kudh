package com.yudildev.logic;

import com.google.common.base.Preconditions;

public class ExerciseTrackerApp {
    private final SessionManager sessionManager;
    private final FeedManager feedManager;
    private final ActivityManager activityManager;
    private final FollowManager followManager;
    private final AccountManager accountManager;
    private final PathManager pathManager;

    private boolean running;

    public ExerciseTrackerApp(SessionManager sessionManager,
                              FeedManager feedManager,
                              ActivityManager activityManager,
                              FollowManager followManager,
                              AccountManager accountManager,
                              PathManager pathManager) {
        this.sessionManager = sessionManager;
        this.feedManager = feedManager;
        this.activityManager = activityManager;
        this.followManager = followManager;
        this.accountManager = accountManager;
        this.pathManager = pathManager;
        this.running = true;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public void exit() {
        running = false;
    }

    public void logout() {
        Preconditions.checkState(sessionManager.isLoggedIn(), "No use logged in, cannot log out");

        sessionManager.logout();

        Preconditions.checkState(!sessionManager.isLoggedIn(), "User is still logged in");
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public FeedManager getFeedManager() {
        return feedManager;
    }

    public  ActivityManager getActivityManager() {
        return activityManager;
    }

    public FollowManager getFollowManager() {
        return followManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public PathManager getPathManager() {
        return pathManager;
    }
}