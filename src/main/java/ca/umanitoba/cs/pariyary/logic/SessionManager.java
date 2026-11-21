package ca.umanitoba.cs.pariyary.logic;

import ca.umanitoba.cs.pariyary.exceptions.*;
import ca.umanitoba.cs.pariyary.model.ExerciseTracker;
import ca.umanitoba.cs.pariyary.model.User;
import com.google.common.base.Preconditions;

public class SessionManager {
    private final ExerciseTracker tracker;
    private User currUser;

    public SessionManager(ExerciseTracker tracker) {

        Preconditions.checkNotNull(tracker, "Tracker can't be null");

        this.tracker = tracker;
        this.currUser = null;
    }

    public void login(String username, String password) throws

            InvalidUsernameException,
            InvalidPasswordException,
            InvalidCredentialsException,
            NoUserFoundException
    {
        validateUsername(username);
        validatePassword(password);

        User user = findUser(username);

        if (user == null) {
            throw new NoUserFoundException();
        }

        if (!user.checkPassword(password)) {
            throw new InvalidCredentialsException();
        }

        currUser = user;

        Preconditions.checkState(currUser.getUsername().equals(username), "Wrong user logged in");
        Preconditions.checkState(isLoggedIn(), "User is still not logged in");
    }

    public User createUser(String username, String password)
            throws InvalidUsernameException, InvalidPasswordException, UserAlreadyExistsException {

        checkNotDuplicateUsername(username);

        User newUser = new User.Builder()
                .username(username)
                .password(password)
                .build();

        tracker.addUser(newUser);
        Preconditions.checkState(findUser(username) != null, "User not created");
        return newUser;
    }

    public void logout() {
        currUser = null;
    }

    public User getCurrUser() {
        return currUser;
    }

    public boolean isLoggedIn() {
        return currUser != null;
    }

    public void validateUsername(String username)
            throws InvalidUsernameException {
        new User.Builder().username(username);
    }

    public void validatePassword(String password)
            throws InvalidPasswordException {
        new User.Builder().password(password);
    }

    public void checkNotDuplicateUsername(String username) throws UserAlreadyExistsException {
        for (User user : tracker.getUsers()) {
            if (user.getUsername().equals(username)) {
                throw new UserAlreadyExistsException();
            }
        }
    }

    private User findUser(String username) {
        for (User user : tracker.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    public ExerciseTracker getTracker() {
        return tracker;
    }
}