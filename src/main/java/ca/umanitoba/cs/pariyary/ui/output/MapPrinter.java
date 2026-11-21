package ca.umanitoba.cs.pariyary.ui.output;

import ca.umanitoba.cs.pariyary.model.Cell;
import ca.umanitoba.cs.pariyary.model.Coordinates;
import ca.umanitoba.cs.pariyary.model.Map;
import ca.umanitoba.cs.pariyary.model.Obstacle;

import java.util.Collection;

public class MapPrinter {

    public void printMap(Map world,
                         Collection<Coordinates> routeCoordinates,
                         Collection<Obstacle> extraObstacles) {

        Cell[][] plane = world.getPlane();

        System.out.println("""
                -----------  LEGEND  -----------
                * - Obstacle Occupied Coordinate
                . - Empty Location
                > - Route of an activity
                """);

        System.out.print("    ");
        for (int col = 0; col < plane[0].length; col++) {
            System.out.printf("%3d", col);
        }
        System.out.println();

        for (int row = 0; row < plane.length; row++) {
            System.out.printf("%3d ", row);

            for (int col = 0; col < plane[row].length; col++) {
                Coordinates current = new Coordinates(col, row);

                boolean inRoute = routeCoordinates != null && routeCoordinates.contains(current);
                boolean inWorldObstacle = !plane[row][col].isEmpty();
                boolean inExtraObstacle = isInExtraObstacles(current, extraObstacles);

                if (inRoute && !inWorldObstacle && !inExtraObstacle) {
                    System.out.printf("%3s", ">");
                } else if (inWorldObstacle || inExtraObstacle) {
                    System.out.printf("%3s", "*");
                } else {
                    System.out.printf("%3s", ".");
                }
            }

            System.out.println();
        }
    }

    private boolean isInExtraObstacles(Coordinates coordinate,
                                       Collection<Obstacle> extraObstacles) {
        boolean found = false;

        if (extraObstacles != null) {
            for (Obstacle obstacle : extraObstacles) {
                if (obstacle.getLocation().contains(coordinate)) {
                    found = true;
                }
            }
        }

        return found;
    }
}