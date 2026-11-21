package ca.umanitoba.cs.pariyary.ui;

import ca.umanitoba.cs.pariyary.exceptions.InvalidPasswordException;
import ca.umanitoba.cs.pariyary.exceptions.InvalidUsernameException;
import ca.umanitoba.cs.pariyary.exceptions.UserAlreadyExistsException;
import ca.umanitoba.cs.pariyary.logic.SessionManager;
import ca.umanitoba.cs.pariyary.model.ExerciseTracker;
import ca.umanitoba.cs.pariyary.model.User;

import java.util.Scanner;

public class CreateUserDisplay {

    private final Scanner input;
    private final SessionManager sessionManager;

    public CreateUserDisplay(Scanner input, SessionManager sessionManager) {
        this.input = input;
        this.sessionManager = sessionManager;
    }

    public void createUser() {

        User user = null;

        while (user == null) {
            String username = getUsername();
            if (username == null) {
                return;
            }
            String password = getPassword();
            if (password == null) {
                return;
            }

            try {
                user = sessionManager.createUser(username, password);

            } catch (InvalidUsernameException e) {
                System.out.println("Invalid username, Try again!");
            } catch (UserAlreadyExistsException e) {
                System.out.println("User already exists, pick another username!");
            } catch (InvalidPasswordException e) {
                System.out.println("Invalid password, Try again!");
            }
        }

    }

    private String getUsername() {
        String username = null;

        while (username == null ) {
            System.out.print("Enter username (to cancel '-1'): ");
            username = input.nextLine();

            if (username.equals("-1")) {
                return null;
            }

            try {
                sessionManager.validateUsername(username);
                sessionManager.checkNotDuplicateUsername(username);
            } catch (InvalidUsernameException e) {
                username = null;
                System.out.println("Invalid username! Try again!");
            } catch (UserAlreadyExistsException e) {
                username = null;
                System.out.println("User already exists, pick another username!");
            }
        }
        return username;
    }

    private String getPassword() {
        String password = null;
        while (password == null) {
            System.out.print("Enter password (to cancel '-1'): ");
            password = input.nextLine();

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
