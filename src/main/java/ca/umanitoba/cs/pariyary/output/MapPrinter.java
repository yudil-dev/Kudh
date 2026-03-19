package ca.umanitoba.cs.pariyary.output;
import ca.umanitoba.cs.pariyary.model.Activity;
import ca.umanitoba.cs.pariyary.model.Coordinates;
import ca.umanitoba.cs.pariyary.model.Map;

import java.util.ArrayList;

public class MapPrinter {

    public void printMap(Map world) {
        int[][] plane = world.getPlane();

        System.out.print("    ");
        for (int col = 0; col < plane[0].length; col++) {
            System.out.printf("%3d", col);
        }
        System.out.println();

        for (int row = 0; row < plane.length; row++) {
            System.out.printf("%3d ", row);
            for (int col = 0; col < plane[row].length; col++) {

                int ele = plane[row][col];

                if (ele == 0) {
                    System.out.printf("%3s", ".");
                } else if (ele == -1) {
                    System.out.printf("%3s", "*");
                } else if (ele == 1) {
                    System.out.printf("%3s", ">");
                }

            }
            System.out.println();
        }
    }

    public void printActivity(Activity activity, Map world) {

        int[][] plane = world.getPlane();
        ArrayList<Coordinates> route = activity.getRoute();

        System.out.print("    ");
        for (int col = 0; col < plane[0].length; col++) {
            System.out.printf("%3d", col);
        }
        System.out.println();

        for (int row = 0; row < plane.length; row++) {
            System.out.printf("%3d ", row);
            for (int col = 0; col < plane[row].length; col++) {

                int ele = plane[row][col];

                if (ele == 0) {
                    System.out.printf("%3s", ".");
                } else if (ele == -1) {
                    System.out.printf("%3s", "*");
                } else if (ele == 1) {
                    if (route.contains(new Coordinates(col, row))) {
                        System.out.printf("%3s", ">");
                    } else {
                        System.out.printf("%3s", ".");
                    }

                }

            }
            System.out.println();
        }
    }
}
