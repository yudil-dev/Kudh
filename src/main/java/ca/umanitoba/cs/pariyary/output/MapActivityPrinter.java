package ca.umanitoba.cs.pariyary.output;
import ca.umanitoba.cs.pariyary.model.*;

import java.util.Collection;

public class MapActivityPrinter {


    public void printMap(Map world, Collection<Coordinates> routeCoordinates) {

        Cell[][] plane =  world.getPlane();
        System.out.println("""
                -----------  LEGEND  -----------
                * - Obstacle Occupied Coordinate
                . - Empty Location
                > - Route of an activity""");
        //Prints header
        System.out.print("    ");
        for (int col = 0; col < plane[0].length; col++) {
            System.out.printf("%3d", col);
        }
        System.out.println();

        for (int row = 0; row < plane.length; row++) {
            System.out.printf("%3d ", row);

            for (int col = 0; col < plane[row].length; col++) {

                Cell cell = plane[row][col];

                if (routeCoordinates.contains(new Coordinates(col, row)) && cell.isEmpty()) {
                    System.out.printf("%3s", ">");
                }
                else {
                    if (cell.isEmpty()) {
                        System.out.printf("%3s", ".");
                    } else {
                        System.out.printf("%3s", "*");
                    }
                }
            }
            System.out.println();
        }
    }
}
