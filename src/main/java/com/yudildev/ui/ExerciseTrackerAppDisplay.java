package com.yudildev.ui;

import com.yudildev.logic.ExerciseTrackerApp;


public class ExerciseTrackerAppDisplay {
    private final ExerciseTrackerApp app;
    private final LoginDisplay loginDisplay;
    private final CreateUserDisplay createUserDisplay;
    private final MainMenuDisplay mainMenuDisplay;
    private final LoggedOutMenuDisplay loggedOutMenuDisplay;
    private final AddActivityDisplay addActivityDisplay;
    private final FollowDisplay followDisplay;
    private final UpdateAccountDisplay updateAccountDisplay;
    private final PathFinderDisplay pathFinderDisplay;

    public ExerciseTrackerAppDisplay(ExerciseTrackerApp app,
                                     LoginDisplay loginDisplay,
                                     LoggedOutMenuDisplay loggedOutMenuDisplay,
                                     CreateUserDisplay createUserDisplay,
                                     MainMenuDisplay mainMenuDisplay,
                                     AddActivityDisplay addActivityDisplay,
                                     FollowDisplay followDisplay,
                                     UpdateAccountDisplay updateAccountDisplay,
                                     PathFinderDisplay pathFinderDisplay) {
        this.app = app;
        this.loginDisplay = loginDisplay;
        this.loggedOutMenuDisplay = loggedOutMenuDisplay;
        this.createUserDisplay = createUserDisplay;
        this.mainMenuDisplay = mainMenuDisplay;
        this.addActivityDisplay = addActivityDisplay;
        this.followDisplay = followDisplay;
        this.updateAccountDisplay =  updateAccountDisplay;
        this.pathFinderDisplay = pathFinderDisplay;
    }

    public void run() {
        while (app.isRunning()) {
            if (!app.isLoggedIn()) {
                runLoggedOutMenu();
            } else {
                runLoggedInMenu();
            }
        }
    }

    private void runLoggedOutMenu() {

        String choice = loggedOutMenuDisplay.getChoice();
        switch (choice) {
            case "1":
                loginDisplay.show();
                break;
            case "2":
                createUserDisplay.createUser();
                break;
            case "3":
                app.exit();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void runLoggedInMenu() {
        String choice = mainMenuDisplay.getChoice(
                app.getFeedManager().getFeed(app.getSessionManager().getCurrUser())
        );

        switch (choice) {
            case "1":
                updateAccountDisplay.show();
                break;
            case "2":
                followDisplay.show();
                break;
            case "3":
                addActivityDisplay.show();
                break;
            case "4":
                pathFinderDisplay.show();
                break;
            case "5":
                app.logout();
                break;
            case "6":
                app.exit();
                break;
            default:
                System.out.println("Invalid option");
        }
    }
}