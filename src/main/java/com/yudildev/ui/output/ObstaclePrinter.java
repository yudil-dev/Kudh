package com.yudildev.ui.output;

import com.yudildev.model.Obstacle;

public class ObstaclePrinter {

    public static void print(Obstacle obstacle) {
        System.out.println("Obstacle ID = " + obstacle.getObstacleID());
        System.out.println("Type = " + obstacle.getType());
        System.out.println("Location = " + obstacle.getLocation());
    }
}
