package com.yudildev.ui;

import com.yudildev.exceptions.ActionCancelledException;
import com.yudildev.exceptions.InvalidCoordinateFormatException;
import com.yudildev.logic.PathManager;
import com.yudildev.model.Coordinates;
import com.yudildev.model.Map;
import com.yudildev.ui.output.MapPrinter;

import java.util.List;
import java.util.Scanner;

public class PathFinderDisplay {

    private final Scanner input;
    private final PathManager pathManager;
    private final Map world;
    private final MapPrinter mapPrinter;

    public PathFinderDisplay(Scanner input,
                             PathManager pathManager,
                             Map world) {
        this.input = input;
        this.pathManager = pathManager;
        this.world = world;
        this.mapPrinter = new MapPrinter();
    }

    public void show() {
        try {
            Coordinates start = getCoordinateWithinMap("Enter start coordinate (x,y) or cancel: ");
            Coordinates end = getCoordinateWithinMap("Enter end coordinate (x,y) or cancel: ");

            String choice = getRouteSourceChoice();

            List<Coordinates> path;

            if (choice.equals("own")) {
                path = pathManager.findPathUsingOwnRoutes(start, end);
            } else {
                path = pathManager.findPathUsingFeedRoutes(start, end);
            }

            if (path.isEmpty()) {
                System.out.println("No path found.");
            } else {
                System.out.println("Path found:");
                mapPrinter.printMap(world, path, null);
            }

        } catch (ActionCancelledException e) {
            System.out.println("Path finding cancelled.");
        }
    }

    private String getRouteSourceChoice() throws ActionCancelledException {
        String choice = "";

        while (choice.isEmpty()) {
            System.out.print("Use your own routes or your feed routes? (own/feed/cancel): ");
            String line = input.nextLine().trim().toLowerCase();

            if (line.equals("cancel")) {
                throw new ActionCancelledException();
            }

            if (line.equals("own") || line.equals("feed")) {
                choice = line;
            } else {
                System.out.println("Invalid input. Please enter own, feed, or cancel.");
            }
        }

        return choice;
    }

    private Coordinates getCoordinateWithinMap(String prompt)
            throws ActionCancelledException {

        Coordinates coordinate = null;

        while (coordinate == null) {
            System.out.print(prompt);
            String line = input.nextLine().trim();

            if (line.equalsIgnoreCase("cancel")) {
                throw new ActionCancelledException();
            }

            try {
                Coordinates parsed = parseCoordinate(line);

                if (!world.coordWithinMap(parsed)) {
                    System.out.println("Coordinates out of map bounds, try again");
                } else {
                    coordinate = parsed;
                }

            } catch (InvalidCoordinateFormatException e) {
                System.out.println("Invalid format. Please enter coordinate as x,y (e.g., 2,3) or cancel.");
            }
        }

        return coordinate;
    }

    private Coordinates parseCoordinate(String line)
            throws InvalidCoordinateFormatException {

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
}