package com.yudildev.ui;

import com.yudildev.exceptions.*;
import com.yudildev.logic.ActivityCreation;
import com.yudildev.logic.ActivityManager;
import com.yudildev.logic.ObstacleCreation;
import com.yudildev.logic.SessionManager;
import com.yudildev.model.*;
import com.yudildev.ui.output.MapPrinter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class AddActivityDisplay {

    private final Scanner input;
    private final ActivityCreation activityCreation;
    private final ObstacleCreation obstacleCreation;
    private final ActivityManager activityManager;
    private final SessionManager sessionManager;
    private final MapPrinter mapPrinter;

    public AddActivityDisplay(Scanner input,
                              SessionManager sessionManager,
                              ActivityCreation activityCreation,
                              ObstacleCreation obstacleCreation,
                              ActivityManager activityManager) {
        this.input = input;
        this.sessionManager = sessionManager;
        this.activityCreation = activityCreation;
        this.obstacleCreation = obstacleCreation;
        this.activityManager = activityManager;
        mapPrinter  = new MapPrinter();
    }

    public void show() {
        try {
            ActivityType type = getActivityType();
            LocalDateTime start = getStartDateTime();
            LocalDateTime end = getEndDateTime(start);

            System.out.println("\nCurrent map with your activity routes and obstacles");

            Collection<Obstacle> obstacles = getEncounteredObstacles();
            List<Coordinates> route = getChosenRoute(obstacles);

            Activity activity = activityCreation.createActivity(type, start, end, route);
            activityManager.addCompletedActivity(activity, obstacles);

            System.out.println("\nThis is your map now, with your added activity\n");
            mapPrinter.printMap(sessionManager.getTracker().getMap(),
                    sessionManager.getCurrUser().getActivitiesOccupiedCoords(), null);
            System.out.println("Activity added successfully.");

        } catch (ActionCancelledException e) {
            System.out.println("Add activity cancelled.");
        } catch (InvalidActivityTypeException e) {
            System.out.println("Invalid activity type.");
        } catch (InvalidStartDateTimeException e) {
            System.out.println("Invalid start date/time.");
        } catch (InvalidEndDateTimeException e) {
            System.out.println("Invalid end date/time.");
        } catch (InvalidRouteException e) {
            System.out.println("Invalid activity route.");
        } catch (Exception e) {
            System.out.println("Unexpected error. Try again");
        }
    }

    private ActivityType getActivityType() throws ActionCancelledException {
        ActivityType type = null;

        while (type == null) {
            System.out.print("Enter activity type (WALK, RUN, BIKE) or 'cancel': ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            try {
                type = ActivityType.fromString(line);
            } catch (InvalidActivityTypeException e) {
                System.out.println("Invalid activity type.");
            }
        }

        return type;
    }

    private List<Coordinates> getChosenRoute(Collection<Obstacle> obstacles)
            throws ActionCancelledException, InvalidCredentialsException, NoActivityFoundException {

        Collection<Activity> previousActivities = activityManager.getCurrUserActivities();

        if (previousActivities.isEmpty()) {
            System.out.println("No previous activities found. Please enter a new route.");
            return getRoute(obstacles);
        }

        String choice = getNewOrOldRouteChoice();

        if (choice.equals("new")) {
            return getRoute(obstacles);
        } else {
            return getExistingRoute(obstacles);
        }
    }

    private String getNewOrOldRouteChoice() throws ActionCancelledException {
        String choice = "";

        while (choice.isEmpty()) {
            System.out.print("Use a new route or an existing route? (new/existing/cancel): ");
            String line = input.nextLine().trim().toLowerCase();

            if (line.equals("cancel")) {
                throw new ActionCancelledException();
            }

            if (line.equals("new") || line.equals("existing")) {
                choice = line;
            } else {
                System.out.println("Invalid input. Please enter new, existing, or cancel.");
            }
        }

        return choice;
    }

    private List<Coordinates> getExistingRoute(Collection<Obstacle> obstacles)
            throws ActionCancelledException, InvalidCredentialsException, NoActivityFoundException {

        printPreviousActivitiesOnMap();

        List<Coordinates> chosenRoute = null;

        while (chosenRoute == null) {

            int activityID = getActivityIDInput();

            try {
                Activity chosenActivity = activityManager.getCurrUserActivityByID(activityID);
                List<Coordinates> routeCopy = new ArrayList<>(chosenActivity.getRoute());

                validateExistingRouteAgainstObstacles(routeCopy, obstacles);

                chosenRoute = routeCopy;
            } catch (RouteCoordinateInObstacleException e) {
                System.out.println("Route goes through current world obstacles, cannot be used");
            } catch (NoActivityFoundException e) {
                System.out.println("No activities found with that ID.");
            } catch (Exception e) {
                System.out.println("Unexpected error. Try again");
            }
        }

        return chosenRoute;
    }

    private void printPreviousActivitiesOnMap() throws InvalidCredentialsException {
        Collection<Activity> activities = activityManager.getCurrUserActivities();

        for (Activity activity : activities) {
            System.out.println("Activity ID: " + activity.getActivityID());
            System.out.println("Type: " + activity.getType());
            System.out.println("Start: " + activity.getStartDateTime());

            mapPrinter.printMap(sessionManager.getTracker().getMap(), activity.getRoute(), null);
            System.out.println();
        }
    }

    private int getActivityIDInput() throws ActionCancelledException {
        Integer activityID = null;

        while (activityID == null) {
            System.out.print("Enter the activity ID to use its route or 'cancel': ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            try {
                activityID = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid activity ID.");
            }
        }

        return activityID;
    }

    private void validateExistingRouteAgainstObstacles(List<Coordinates> route, Collection<Obstacle> obstacles)
            throws CoordinateOutOfBoundsException,
            DuplicateCoordinateException,
            RouteCoordinateInObstacleException,
            DiscontinousRouteException {

        List<Coordinates> checkedRoute = new ArrayList<>();

        for (Coordinates coordinate : route) {
            activityCreation.validateNextCoordinate(coordinate, checkedRoute, obstacles);
            checkedRoute.add(coordinate);
        }
    }

    private List<Coordinates> getRoute(Collection<Obstacle> obstacles) throws ActionCancelledException {

        System.out.println("\nMap with your obstacles added and your previous activities\n");
        mapPrinter.printMap(sessionManager.getTracker().getMap(),
                sessionManager.getCurrUser().getActivitiesOccupiedCoords(), obstacles);

        List<Coordinates> route = new ArrayList<>();
        boolean done = false;

        while (!done) {
            System.out.print("Enter route coordinate (x,y), done, or cancel: ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            if (line.equalsIgnoreCase("done")) {
                if (route.isEmpty()) {
                    System.out.println("Route cannot be empty.");
                } else {
                    done = true;
                }
            } else {
                try {
                    Coordinates coordinate = parseCoordinate(line);
                    activityCreation.validateNextCoordinate(coordinate, route, obstacles);
                    route.add(coordinate);
                    System.out.println("Added.");
                } catch (InvalidCoordinateFormatException e) {
                    System.out.println("Invalid format. Use x,y");
                } catch (CoordinateOutOfBoundsException e) {
                    System.out.println("Route coordinates have to be within map bounds!");
                } catch (DuplicateCoordinateException e) {
                    System.out.println("Duplicate coordinate, coordinate already exists in route!");
                } catch (DiscontinousRouteException e) {
                    System.out.println("Didn't know you could teleport!?? Routes have to be continuous! ");
                } catch (RouteCoordinateInObstacleException e) {
                    System.out.println("You cannot go through obstacles!");
                }
            }
        }

        return route;
    }



    private Collection<Obstacle> getEncounteredObstacles () {

        mapPrinter.printMap(sessionManager.getTracker().getMap(), null, null);
        Collection<Obstacle> obstacles = new ArrayList<>();

        String answer = getYesNoInput("Did you encounter obstacles? (yes/no): ");

        if (answer.equalsIgnoreCase("no")) {
            return obstacles;
        }

        boolean addMore = true;

        while (addMore) {
            try {
                Obstacle obstacle = getSingleObstacle();
                obstacles.add(obstacle);
                System.out.println("Obstacle added.");
            } catch (ActionCancelledException e) {
                System.out.println("Obstacle creation cancelled.");
            } catch (InvalidObstacleTypeException e) {
                System.out.println("Invalid Obstacle.");
            }

            String input = getYesNoInput("Add another obstacle? (yes/no): ");
            addMore = input.trim().equalsIgnoreCase("yes");
        }

        return obstacles;
    }

    private String getYesNoInput(String prompt) {
        boolean validInput = false;
        String answer = "";

        while (!validInput) {
            System.out.print(prompt);
            answer = input.nextLine().trim().toLowerCase();

            if (answer.equals("yes") || answer.equals("no")) {
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter yes or no.");
            }

        }
        return answer;
    }

    private Obstacle getSingleObstacle()
            throws InvalidObstacleTypeException, ActionCancelledException {

        Obstacle obstacle = null;
        ObstacleType type = getObstacleType();
        List<Coordinates> coords = new ArrayList<>();

        while (obstacle == null) {

            System.out.println("Obstacles coordinates: " +coords);
            String line = getCoordinateDoneCancelInput("Enter obstacle coordinates (x,y), done, or cancel (obstacles can occupy multiple" +
                    " coordinates and need to be rectangular): ");

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            if (line.equalsIgnoreCase("done")) {
                if (coords.isEmpty()) {
                    System.out.println("Obstacle cannot be empty.");
                } else {
                    try {
                        obstacle = obstacleCreation.createObstacle(type, coords);
                    } catch (InvalidObstacleShapeException e) {
                        System.out.println("Invalid obstacle shape (must form a rectangle).");
                        coords.clear();
                    } catch (EmptyObstacleLocationException e) {
                        System.out.println("Obstacle location cannot be empty");
                    } catch (Exception e) {
                        System.out.println("Invalid location.");
                    }
                }
            } else {
                try {
                    Coordinates coordinate = parseCoordinate(line);
                    obstacleCreation.validateNextCoordinate(coordinate, coords);
                    coords.add(coordinate);
                    System.out.println("Added");
                } catch (InvalidCoordinateFormatException e) {
                    System.out.println("Invalid coordinate format, eg: 2,3.");
                } catch (CoordinateOutOfBoundsException e) {
                    System.out.println("Coordinate out of maps bounds, enter a coordinate within the map");
                } catch (ObstacleCoordInPrevObstacle e) {
                    System.out.println("Another obstacle exist in that coordinate!");
                } catch (DuplicateCoordinateException e) {
                    System.out.println("Duplicate obstacle coordinate!");
                }
            }
        }

        return obstacle;
    }

    private ObstacleType getObstacleType () throws ActionCancelledException {

        ObstacleType type = null;

        while (type == null) {
            System.out.print("Enter obstacle type (TREE, BUILDING, ROCK) or cancel: ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            try {
                type = ObstacleType.fromString(line);
            } catch (InvalidObstacleTypeException e) {
                System.out.println("Invalid obstacle type.");
            }
        }
        return type;
    }

    private String getCoordinateDoneCancelInput(String prompt)
            throws ActionCancelledException {

        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            if (line.equalsIgnoreCase("done")) {
                return "done";
            }

            try {
                parseCoordinate(line);
                return line;
            } catch (InvalidCoordinateFormatException e) {
                System.out.println("Invalid format. Please enter coordinate as x,y (e.g., 2,3) or cancel.");
            }
        }
    }

    private Coordinates parseCoordinate(String line) throws InvalidCoordinateFormatException {
        String[] parts = line.split(",");

        if (parts.length != 2) {
            throw new InvalidCoordinateFormatException();
        }

        int x;
        int y;

        try {
            x = Integer.parseInt(parts[0].trim());
            y = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new InvalidCoordinateFormatException();
        }

        return new Coordinates(x, y);
    }

    private LocalDateTime getStartDateTime() throws ActionCancelledException{
        LocalDateTime start = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (start == null) {

            System.out.print("Enter start (yyyy-MM-dd HH:mm) or 'cancel': ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            try {
                start = LocalDateTime.parse(line, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Try again (eg: 2004-04-13 10:10)");
            }
        }
        return start;
    }

    private LocalDateTime getEndDateTime(LocalDateTime start) throws ActionCancelledException{
        LocalDateTime end = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        while (end == null) {
            System.out.print("Enter end (yyyy-MM-dd HH:mm) or 'cancel': ");
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            try {

                end = LocalDateTime.parse(line, formatter);

                if (!start.isBefore(end)) {
                    System.out.println("End must be after start.");
                    end = null;
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Try again (eg: 2004-04-13 10:10)");
            }
        }
        return end;
    }
}