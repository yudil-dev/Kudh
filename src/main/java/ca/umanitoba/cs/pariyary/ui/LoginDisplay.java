package ca.umanitoba.cs.pariyary.ui;
import ca.umanitoba.cs.pariyary.exceptions.InvalidCredentialsException;
import ca.umanitoba.cs.pariyary.exceptions.InvalidPasswordException;
import ca.umanitoba.cs.pariyary.exceptions.InvalidUsernameException;
import ca.umanitoba.cs.pariyary.exceptions.NoUserFoundException;
import ca.umanitoba.cs.pariyary.logic.SessionManager;

import java.util.Scanner;

public class LoginDisplay {

    private final Scanner input;
    private final SessionManager sessionManager;

    public LoginDisplay(Scanner input, SessionManager sessionManager) {
        this.input = input;
        this.sessionManager = sessionManager;
    }

    public void show() {

        while (!sessionManager.isLoggedIn()) {
            String username = getUsername();

            if (username == null) {
                return;
            }

            String password = getPassword();
            if (password == null) {
                return;
            }

            try {
                sessionManager.login(username, password);
                System.out.println("login successful!");
            } catch (InvalidCredentialsException | InvalidPasswordException |
                     InvalidUsernameException e) {
                System.out.println("Invalid username or password. Try again");
            } catch (NoUserFoundException e) {
                System.out.println("No user found. Try again with another username or exit and create a user");
            }
        }
    }


    private String getUsername() {

        String username = null;
        while (username == null) {
            System.out.print("Enter username (-1 to cancel): ");
            username = input.nextLine().strip();

            if (username.equals("-1")) {
                return null;
            }

            try {
                sessionManager.validateUsername(username);
            } catch (InvalidUsernameException e) {
                username = null;
                System.out.println("Invalid username! Try again!");
            }
        }
        return username;
    }

    private String getPassword() {

        String password = null;
        while (password == null) {
            System.out.print("Enter password (-1 to cancel): ");
            password = input.nextLine().strip();

            if (password.equals("-1")) {
                return null;
            }

            try {
                sessionManager.validatePassword(password);
            } catch (InvalidPasswordException e) {
                password = null;
                System.out.println("Invalid password! Try again!");
            }
        }
        return password;
    }
}


