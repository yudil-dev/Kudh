package com.yudildev.model;

import com.yudildev.exceptions.InvalidObstacleTypeException;

public enum ObstacleType {
    TREE,
    BUILDING,
    ROCK;

    public static ObstacleType fromString(String obsType) throws InvalidObstacleTypeException {
        return switch(obsType.trim().toLowerCase()) {
            case "tree" -> ObstacleType.TREE;
            case "building" -> ObstacleType.BUILDING;
            case "rock" -> ObstacleType.ROCK;
            default -> throw new InvalidObstacleTypeException();
        };
    }
}
