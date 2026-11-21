package com.yudildev.model;

public class Cell {

    private Obstacle obstacle;

    // can add and remove activity with the setter
    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public boolean isEmpty() {
        return (obstacle == null);
    }
}
