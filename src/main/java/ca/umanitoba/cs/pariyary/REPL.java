package ca.umanitoba.cs.pariyary;

import ca.umanitoba.cs.pariyary.model.*;
import ca.umanitoba.cs.pariyary.output.MapPrinter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Scanner;

import static ca.umanitoba.cs.pariyary.output.UserPrinter.printSummaryUserActivity;

public class REPL {

    public static void main(String[] args) {

        REPL repl = new REPL();
        repl.startREPL();

    }

    ExerciseTracker et = new ExerciseTracker();
    MapPrinter mp = new MapPrinter();
    Scanner scanner = new Scanner(System.in);

    public void startREPL() {

        boolean end = false;
        while (!end) {


            if (et.getMap() != null) {

                String userInput = scanner.nextLine().toUpperCase().trim();

                if (userInput.equals("HELP"))  {// help
                    System.out.println("""
                \nADD OBSTACLE - Creates an obstacle and adds it to Map, prompts to enter everything required for an Obstacle
                
                ADD ACTIVITY - Creates an obstacle and adds it to Map, prompts to enter everything required for an Activity
                
                SHOW MAP - Prints out a visual representation of the map as a grid of ASCII symbols
                
                SHOW OBSTACLES - Prints out the list of obstacles and their properties
                
                SHOW ACTIVITIES - Prints out the list of activities and their properties
                
                SHOW ACTIVITY - Lists all activities available, prompts you to enter an activity ID, prints selected activity on Map
                
                REMOVE ACTIVITY - Prompts you to select an activity and removes it
                
                REMOVE OBSTACLE - Prompts you to select an obstacle and removes it
                
                REMOVE MAP - Deletes current map
                """);
                }

                else if (userInput.equals("ADD OBSTACLE")) { // Add obstacle
                    addObstacleREPL();
                }

                else if (userInput.equals("ADD ACTIVITY")) { //Add Activity
                    addActivityREPL();
                    System.out.println("Activity Successfully Added...");
                }

                else if (userInput.equals("SHOW MAP")) { //Show map
                    showMapREPL();
                }

                else if (userInput.equals("SHOW OBSTACLES")) {// Show obstacles
                    showObstaclesREPL();
                }

                else if (userInput.equals("SHOW ACTIVITIES")) {//Show activities
                    showActivitiesREPL();
                }

                else if (userInput.equals("SHOW ACTIVITY")) { //Show Activity
                    showActivityREPL();
                }

                else if (userInput.equals("REMOVE ACTIVITY")) {//Remove Activities
                    removeActivityREPL();
                    System.out.println("Activity Successfully Removed...");
                }

                else if (userInput.equals("REMOVE OBSTACLE")) {//Remove obstacle
                    removeObstacleREPL();
                    System.out.println("Obstacle Removed Successfully...");
                }

                else if (userInput.equals("REMOVE MAP")) { // remove map
                    et.removeWorld();
                    System.out.println("Map Removed Successfully...");
                }
                else {
                    System.out.print("Unknown Command, Type HELP for list of available commands\n");
                }

            } else {

                String userInput = scanner.nextLine().toUpperCase().trim();

                if (userInput.equals("ADD MAP")) {
                    addMapREPL();
                    System.out.println("Map Created Sucessfully..");
                } else if (userInput.equals("HELP")) {
                    print1stHelpMenu();
                } else {
                    System.out.print("Unknown Command, Type HELP for list of available commands\n");
                }

            }

        }
    }

    private void print1stHelpMenu() {
        System.out.println("ADD MAP - Prompts you to enter Map Height and Width, Creates Map accordingly. Unveils rest of the available commands");
    }

    public Map createUserInputMap() {

        System.out.print("\nEnter Map Height (Number Greater than 0): ");
        final int HEIGHT = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Map Width (Number Greater than 0): ");
        final int WIDTH = Integer.parseInt(scanner.nextLine());
        System.out.println();

        return new Map(WIDTH, HEIGHT);

        //Input Validation
//        Map userMap = null;
//        try {
//            System.out.print("\nEnter Map Height: ");
//            final int HEIGHT = Integer.parseInt(scanner.nextLine());
//
//            System.out.print("Enter Map Width: ");
//            final int WIDTH = Integer.parseInt(scanner.nextLine());
//            System.out.println();
//
//            userMap = new Map(WIDTH, HEIGHT);
//        } catch (NumberFormatException e) {
//            System.out.print("Invalid Input, try again: ");
//        }
//
//        return userMap;
    }

    public int getActivityID() {
        Activity activity = null;
        System.out.println();
        HashMap<Integer, Activity> activityDict = et.getUser().getHistoryActivity();

        int activityID = -1;

        if (!activityDict.isEmpty()) {
//            mp.printMap(et.getMap());

            for (Activity a : activityDict.values()) {
                System.out.println(a + "\n");
            }
            System.out.println("Enter Activity ID: ");
            activityID = getIntInput();
            System.out.println();

            while (!activityDict.containsKey(activityID)) {
                System.out.print("Invalid Input, try again: ");
                activityID = getIntInput();
            }
        }

        return activityID;
    }

    public int getObstacleIDToRemove() {

        System.out.println();
        HashMap<Integer, Obstacle> obsIDDict = et.getMap().getObstacles();

        int obstacleID = -1;
        if (!obsIDDict.isEmpty()) {

//            mp.printMap(et.getMap());

            for (Obstacle o : obsIDDict.values()) {
                System.out.println(o + "\n");
            }
            System.out.print("Enter ID to Remove Obstacle: ");
            obstacleID = getIntInput();
            System.out.println();

            while (!obsIDDict.containsKey(obstacleID)) {
                System.out.print("Invalid Input, try again: ");
                obstacleID = getIntInput();
            }

        }
        return obstacleID;
    }

    public Obstacle createUserInputObstacle() {

        Obstacle obstacle = new Obstacle();

        //get coordinates from user
        boolean end = false;
        while (!end) {

            System.out.println("\n" + obstacle);
            System.out.print("""
                    1 Add Coordinates
                    2 Remove Coordinates
                    0 Finalize Obstacle (Exit)
                    
                    Enter (1, 2 or 0):\s""");

            int userSelection = getIntInput();

            if (userSelection == 1) {

                obstacle.addCoord(createUserInputCoord());

            } else if (userSelection == 2) {
                obstacle.removeCoord(createUserInputCoord());
            }else if (userSelection == 0) {
                //uncommenting this piece of code makes it so that the invariant doesn't get triggered
//                if (obstacle.getLocation().isEmpty()) {
//                    System.out.println("Obstacle must contain at least one coordinate.\n");
//                } else {
                end = true;

            } else {
                System.out.print("Invalid Input, Try again: ");
            }
        }

        obstacle.checkObstacleFinalization();

        System.out.printf("Obstacle Created Successfully\n%s\n", obstacle);
        return obstacle;
    }

    public ActivityType getUserActitiyType() {

        System.out.println("\nEnter Activity Type (walk, run, bike): ");
        var stringActivityType = scanner.nextLine().toUpperCase();
        return ActivityType.valueOf(stringActivityType);

//        ActivityType activityType = null;
//
//        while (activityType == null) {
//
//            System.out.println("\nEnter Activity Type (walk, run, bike): ");
//            var stringActivityType = scanner.nextLine().toUpperCase();
//
//            try {
//                activityType = ActivityType.valueOf(stringActivityType);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Invalid activity type, try again: ");
//            }
//        }
//
//        return activityType;
    }

    public LocalDateTime getUserDT() {

        LocalDateTime dt = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        boolean valid = false;

        while (!valid) {
            System.out.print("Enter date and time (yyyy-MM-dd HH:mm):");
            String input = scanner.nextLine();

            try {
                dt = LocalDateTime.parse(input, formatter);
                valid = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time. Try again");
            }
        }

        return dt;
    }

    public Activity createUserInputActivity() {

        ActivityType activityType = getUserActitiyType();
        System.out.print("Start Date  ");
        LocalDateTime startDT = getUserDT();
        System.out.print("End Date  ");
        LocalDateTime endDT = getUserDT();

        Activity activity = new Activity(activityType, startDT, endDT);

        boolean end = false;

        while (!end) {

            System.out.println("\n" + activity);
            System.out.print("""
                    \n1 Add Coordinates
                    2 Remove Coordinates
                    0 Finalize Route (Exit)
                    
                    Enter (1, 2 or 0):\s""");

            int userSelection = getIntInput();

            if (userSelection == 1) {
                activity.addCoordinates(createUserInputCoord());
            }  else if (userSelection == 2) {
                activity.removeCoordinates(createUserInputCoord());
            } else if (userSelection == 0) {
                end = true;
            } else {
                System.out.print("Invalid Input, Try again: ");
            }
        }

        activity.checkActivity();
        return activity;
    }

    public int getIntInput() {

        boolean validInput = false;
        int value = 0;

        while (!validInput) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number, try again: ");
            }
        }
        return value;
    }

    public Coordinates createUserInputCoord() {

        System.out.print("Enter coordinates in the format 'X Y': ");
        String coordString = scanner.nextLine();
        System.out.println();

        Coordinates coord = new Coordinates(coordString);

        return coord;

        //Input Validation
        //Coordinates coord = null;
        //boolean invalidInput = true;
//        while (invalidInput) {
//            System.out.print("Enter coordinates in the format 'X Y': ");
//            String coordString = scanner.nextLine();
//            System.out.println();
//
//            try {
//                coord = new Coordinates(coordString);
//                invalidInput = false;
//            } catch (IllegalArgumentException iae) {
//                System.out.print("Invalid coordinates format, Try again: ");
//            }
//
//        }

    }

    public void print2ndMenu() {
        System.out.print("""
                        \n1 Help
                        2 ADD OBSTACLE
                        3 ADD ACTIVITY
                        4 SHOW MAP
                        5 SHOW OBSTACLES
                        6 SHOW ACTIVITIES
                        7 SHOW ACTIVITY
                        8 REMOVE ACTIVITIES
                        9 REMOVE OBSTACLES
                        10 REMOVE MAP
                        11 EXIT
                        Enter your choice (any number from 1 - 11):\s""");
    }

    public void print1stMenu() {
        System.out.print("""
                \n1. Help
                2. Add Map
                Enter your choice (1 or 2):\s""");
    }

    public void addObstacleREPL() {
        Obstacle obstacle = createUserInputObstacle();
        System.out.println("Adding to Map...");
        et.getMap().addObstacle(obstacle);
        System.out.println("Successfully added!");
        System.out.println();
//        mp.printMap(et.getMap());
    }

    public void removeObstacleREPL() {
        int idToRemove = getObstacleIDToRemove();
        if (idToRemove != -1) {
            et.getMap().removeObstacle(et.getMap().getObstacles().get(idToRemove));
        }
    }

    public void addMapREPL() {
        et.setWorld(createUserInputMap());
//        if (et.getMap() != null) {
//            mp.printMap(et.getMap());
//        }
    }

    public void showObstaclesREPL() {
        System.out.println();

        for (Obstacle o : et.getMap().getObstacles().values()) {
            System.out.println(o + "\n");
        }
    }

    public void showActivitiesREPL() {
        System.out.println();

        for (Activity a : et.getUser().getHistoryActivity().values()) {
            System.out.println(a + "\n");
        }
    }

    public void showActivityREPL() {
        int aIDToShow = getActivityID();
        mp.printActivity(et.getUser().getHistoryActivity().get(aIDToShow) , et.getMap());
    }

    public void addActivityREPL() {
        Activity activity = createUserInputActivity();
        et.addActivityToUserAndPlane(activity);
        System.out.println();
//        mp.printMap(et.getMap());
    }

    public void removeActivityREPL() {
        int idToRemove = getActivityID();
        if  (idToRemove != -1) {
            et.removeActivityFromUserAndPlane(et.getUser().getHistoryActivity().get(idToRemove));
        }


    }

    public void showMapREPL() {
        System.out.println("""
                \n* - Obstacle Occupied Coordinate
                . - Empty Location
                > - Route of an activity""");
        System.out.println();
        mp.printMap(et.getMap());
        System.out.println();
        printSummaryUserActivity(et.getUser());
    }
}
