package ca.umanitoba.cs.pariyary.model;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Map {


    private final Cell[][] plane;
    private final int width;
    private final int height;

    private final HashMap<Integer, Obstacle> obstacles;
    private final Collection<Coordinates> occupiedCoords;


    public Map(int width, int height) {

        occupiedCoords = new HashSet<>();

        this.width = width;
        this.height = height;

        plane = new Cell[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                plane[x][y] = new Cell(); // blank cell
            }
        }

        obstacles = new HashMap<>();

        checkMap();
    }


    public void addObstacle(Obstacle obstacle) {

        checkObstacle(obstacle);
        for (Coordinates coordinate : obstacle.getLocation()) {
            occupiedCoords.add(coordinate);
            int x = coordinate.getX();
            int y = coordinate.getY();
            plane[y][x].setObstacle(obstacle);
        }
        obstacles.put(obstacle.getObstacleID(), obstacle);

    }

    public boolean coordWithinMap(Coordinates coordinates) {
        boolean inMap = false;

        int x = coordinates.getX();
        int y = coordinates.getY();

        if (0 <= x && x < width &&
                0 <= y && y < height) {
            inMap = true;
        }

        return inMap;
    }

    //Invariants
    private void checkMap() {
        Preconditions.checkNotNull(plane, "Plane can't be null");
        Preconditions.checkState(width > 0, "Plane Width can't be 0");
        Preconditions.checkState(height > 0, "Plane Height can't be 0");

        checkObstacles();
    }

    private boolean obsWithinMap(Obstacle obstacle) {

        boolean inMap = true;
        var location = obstacle.getLocation();

        int i = 0;
        while (i < location.size() && inMap) {
            Coordinates c = location.get(i);
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

    public boolean isObsRectangle(Obstacle o) {
        var location = o.getLocation();
        boolean isRectangle = false;

        if (!location.isEmpty()) {

            int minX = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;

            for (Coordinates c : location) {
                minX = Math.min(minX, c.getX());
                maxX = Math.max(maxX, c.getX());
                minY = Math.min(minY, c.getY());
                maxY = Math.max(maxY, c.getY());
            }

            isRectangle = true;

            for (int x = minX; x <= maxX && isRectangle; x++) {
                for (int y = minY; y <= maxY && isRectangle; y++) {
                    if (!location.contains(new Coordinates(x, y))) {
                        isRectangle = false;
                    }
                }
            }
        }

        return isRectangle;
    }


    public boolean coordInObstacle(Coordinates coordinate) {
        Preconditions.checkNotNull(coordinate, "coordinate cannot be null");

        for (Obstacle obstacle : obstacles.values()) {
            if (obstacle.getLocation().contains(coordinate)) {
                return true;
            }
        }
        return false;
    }


    //Getters
    public HashMap<Integer, Obstacle> getObstacles() {
        return obstacles;
    }

    public Cell[][] getPlane() {
        return plane;
    }

    public Collection<Coordinates> getOccupiedCoords() {
        return occupiedCoords;
    }

}
