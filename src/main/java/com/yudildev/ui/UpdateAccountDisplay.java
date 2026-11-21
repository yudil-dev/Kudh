package com.yudildev.ui;

import com.yudildev.exceptions.*;
import com.yudildev.logic.AccountManager;
import com.yudildev.logic.SessionManager;
import com.yudildev.model.Activity;
import com.yudildev.model.User;
import com.yudildev.ui.output.MapPrinter;

import java.util.Scanner;

public class UpdateAccountDisplay {

    private final Scanner input;
    private final AccountManager accountManager;
    private final SessionManager sessionManager;
    private final MapPrinter mapPrinter;

    public UpdateAccountDisplay(Scanner input,
                                AccountManager accountManager,
                                SessionManager sessionManager
                                ) {
        this.input = input;
        this.accountManager = accountManager;
        this.sessionManager = sessionManager;
        this.mapPrinter = new MapPrinter();
    }

    public void show() {
        boolean updating = true;



        while (updating) {
            printUserActivitiesMap();
            printMenu();

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    changeUsername();
                    break;
                case "2":
                    changePassword();
                    break;
                case "3":
                    unfollowSomeone();
                    break;
                case "4":
                    removeActivity();
                    break;
                case "5":
                    updating = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Update Account ---");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Unfollow Someone");
        System.out.println("4. Remove Prior Activity");
        System.out.println("5. Back");
        System.out.print("Enter choice: ");
    }

    private void changeUsername() {
        try {
            System.out.print("Enter new username: ");
            String username = input.nextLine().trim();

            accountManager.changeUsername(username);
            System.out.println("Username updated.");
        } catch (InvalidUsernameException e) {
            System.out.println("Username cannot be blank");
        } catch (UserAlreadyExistsException e) {
            System.out.println("Username already exists. Try another username");
        }
    }

    private void changePassword() {
        try {
            System.out.print("Enter new password: ");
            String password = input.nextLine().trim();

            accountManager.changePassword(password);
            System.out.println("Password updated.");
        } catch (InvalidPasswordException e) {
            System.out.println("Password cannot be blank, try again");
        }
    }

    private void unfollowSomeone() {
        printFollowedUsers();
        try {
            System.out.print("Enter username to unfollow: ");
            String username = input.nextLine().trim();

            accountManager.unfollowUser(username);
            System.out.println("User unfollowed.");
        } catch (NoUserFoundException e) {
            System.out.println("You do not follow that user!");
        } catch (InvalidUsernameException e) {
            System.out.println("Username cannot be blank");
        }
    }

    private void printFollowedUsers() {
        User currUser = sessionManager.getCurrUser();

        if (currUser.getFollowedUsers().isEmpty()) {
            System.out.println("You are not following anyone");
            return;
        }

        System.out.println("\n--- Users You Follow ---");

        for (User user : currUser.getFollowedUsers()) {
            System.out.println("- " + user.getUsername());
        }
    }

    private void removeActivity() {
        printActivitiesIndividually();

        try {
            System.out.print("Enter activity ID to remove: ");
            int id = -1;
            try {
                id = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid activity ID, enter a number!");
            }

            accountManager.removeActivity(id);
            System.out.println("Activity removed.");
        } catch (NoActivityFoundException e) {
            System.out.println("No such activity exists!");
        }
    }

    private void printUserActivitiesMap() {
        User currUser = sessionManager.getCurrUser();

        System.out.println("\n--- Your Activity Map ---");
        mapPrinter.printMap(sessionManager.getTracker().getMap(),
                currUser.getActivitiesOccupiedCoords(),
                null);
    }

    private void printActivitiesIndividually() {
        User currUser = sessionManager.getCurrUser();

        if (currUser.getHistoryActivity().isEmpty()) {
            System.out.println("You have no activities to remove.");
            return;
        }

        System.out.println("\n--- Your Activities ---");

        for (Activity activity : currUser.getHistoryActivity().values()) {
            System.out.println("Activity ID: " + activity.getActivityID());
            System.out.println("Type: " + activity.getType());
            System.out.println("Start: " + activity.getStartDateTime());

            mapPrinter.printMap(sessionManager.getTracker().getMap(),
                    activity.getRoute(),
                    null);

            System.out.println();
        }
    }

}