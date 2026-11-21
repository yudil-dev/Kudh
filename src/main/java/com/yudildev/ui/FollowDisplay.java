package com.yudildev.ui;

import com.yudildev.exceptions.ActionCancelledException;
import com.yudildev.exceptions.AlreadyFollowingUserException;
import com.yudildev.exceptions.CannotFollowSelfException;
import com.yudildev.exceptions.NoUserFoundException;
import com.yudildev.logic.FollowManager;
import com.yudildev.model.User;

import java.util.List;
import java.util.Scanner;

public class FollowDisplay {

    private final Scanner input;
    private final FollowManager followManager;

    public FollowDisplay(Scanner input, FollowManager followManager) {
        this.input = input;
        this.followManager = followManager;
    }

    public void show() {
        try {
            List<User> followableUsers = followManager.getFollowableUsers();

            if (followableUsers.isEmpty()) {
                System.out.println("No users available to follow.");
                return;
            }

            printFollowableUsers(followableUsers);

            String username = getUsernameToFollow();
            followManager.followUser(username);

            System.out.println("User followed successfully.");
        } catch (ActionCancelledException e) {
            System.out.println("Follow cancelled.");
        } catch (NoUserFoundException e) {
            System.out.println("No such user found.");
        } catch (CannotFollowSelfException e) {
            System.out.println("You cannot follow yourself.");
        } catch (AlreadyFollowingUserException e) {
            System.out.println("You are already following that user.");
        }
    }

    private void printFollowableUsers(List<User> users) {
        System.out.println("\n--- Users You Can Follow ---");
        for (User user : users) {
            System.out.println("- " + user.getUsername());
        }
    }

    private String getUsernameToFollow() throws ActionCancelledException {
        String username = null;

        while (username == null) {
            System.out.print("Enter username to follow or 'cancel': ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            if (line.isEmpty()) {
                System.out.println("Username cannot be empty.");
            } else {
                username = line;
            }
        }

        return username;
    }
}