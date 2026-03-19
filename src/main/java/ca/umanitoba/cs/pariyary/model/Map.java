package ca.umanitoba.cs.pariyary.model;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Map {

    public static HashSet<Coordinates> occupiedCoords = new HashSet<>();

    private final int PLANE_WIDTH;
    private final int PLANE_HEIGHT;
    private final int[][] plane;
    private final HashMap<Integer, Obstacle> obstacles;
    private Coordinates userLocation;

    public Map(int PLANE_WIDTH, int PLANE_HEIGHT) {

        this.PLANE_WIDTH = PLANE_WIDTH;
        this.PLANE_HEIGHT = PLANE_HEIGHT;

        plane = new int[PLANE_HEIGHT][PLANE_WIDTH];
        obstacles = new HashMap<>();

        checkMap();
    }


    public void addObstacle(Obstacle obstacle) {
        checkObstacle(obstacle);

        for (Coordinates coordinate : obstacle.getLocation()) {
            occupiedCoords.add(coordinate);
            int x = coordinate.getX();
            int y = coordinate.getY();
            plane[y][x] = -1;
        }
        obstacles.put(obstacle.getObstacleID(), obstacle);

    }

    public void removeObstacle(Obstacle obstacle) {

        if (obstacle != null && obstacles.containsKey(obstacle.getObstacleID())) {
            for (Coordinates coordinate : obstacle.getLocation()) {
                occupiedCoords.remove(coordinate);
                int x = coordinate.getX();
                int y = coordinate.getY();
                plane[y][x] = 0;
            }
            obstacles.remove(obstacle.getObstacleID());
        } else {
            System.out.println("Obstacle doesn't exist");
        }

    }


    //change this to list<coordinates>
    public void addRouteToPlane(List<Coordinates> route) {

        for (var c : route) {
            plane[c.getY()][c.getX()] = 1;
        }
    }

    public void removeRouteFromPlane(List<Coordinates> route) {
        for (var c : route) {
            plane[c.getY()][c.getX()] = 0;
        }
    }



    //Invariants
    public void checkMap() {
        Preconditions.checkNotNull(plane, "Plane can't be null");
        Preconditions.checkState(PLANE_WIDTH > 0, "Plane Width can't be 0");
        Preconditions.checkState(PLANE_HEIGHT > 0, "Plane Height can't be 0");

//        Preconditions.checkNotNull(userLocation, "userLocation can't be null");
//        Preconditions.checkState(withinMap(userLocation), "userLocation has to be within Map's bounds");

        checkObstacles();
    }


    public boolean coordWithinMap(Coordinates coordinates) {
        boolean inMap = false;

        int x = coordinates.getX();
        int y = coordinates.getY();

        if (0 <= x && x < PLANE_WIDTH &&
                0 <= y && y < PLANE_HEIGHT)
        {
            inMap = true;
        }

        return inMap;
    }


    public boolean obsWithinMap(Obstacle obstacle) {

        boolean inMap = true;
        var location = obstacle.getLocation();

        int i = 0;
        while (i < location.size() && inMap) {
            Coordinates c =  location.get(i);
            if (!coordWithinMap(c)) {
                inMap = false;
            }
            i++;
        }

        return inMap;
    }

    private void checkObstacles() {
        for (var obstacle : obstacles.values()) {
            checkObstacle(obstacle);
        }
    }

    private void checkObstacle(Obstacle obstacle) {
        Preconditions.checkNotNull(obstacle, "Obstacle can't be null");
        Preconditions.checkState(obsWithinMap(obstacle), "Obstacle out of bounds");
        Preconditions.checkState(isValidObsLocation(obstacle), "Obstacle Coordinates already occupied");
        Preconditions.checkState(isObsRectangle(obstacle), "Obstacle has to be rectangular");
    }

    private boolean isValidObsLocation(Obstacle obstacle) {

        var location = obstacle.getLocation();
        boolean valid = true;

        if (!occupiedCoords.isEmpty()) {

            int i = 0;
            while (i < location.size() && valid) {

                var c = location.get(i);
                if (occupiedCoords.contains(c)) {
                    valid = false;
                }

                i++;
            }
        }

        return valid;
    }

    private boolean isObsRectangle(Obstacle o) {
        var location = o.getLocation();
        boolean isRectangle = false;

        if (!location.isEmpty()) {

            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;

            // Find bounds
            for (Coordinates c : location) {
                minX = Math.min(minX, c.getX());
                maxX = Math.max(maxX, c.getX());
                minY = Math.min(minY, c.getY());
                maxY = Math.max(maxY, c.getY());
            }

            int rectangleArea = (maxX - minX + 1) * (maxY - minY + 1);
            // Rectangle must contain every point in the bounding box
            if (location.size() == rectangleArea) {
                isRectangle = true;
            }
        }

        return isRectangle;
    }


    //Exercise Tracker Calls for its Invariants
    public boolean isObstacleBlockRoute(List<Coordinates> route) {

        for (Coordinates c : route) {
            if (occupiedCoords.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRouteWithinMap(List<Coordinates> route) {
        for (Coordinates c : route) {
            if (!coordWithinMap(c)) {
                return false;
            }
        }
        return true;
    }


    //Getters
    public HashMap<Integer, Obstacle> getObstacles() {
        return obstacles;
    }

    public int[][] getPlane() {
        return plane;
    }
}
