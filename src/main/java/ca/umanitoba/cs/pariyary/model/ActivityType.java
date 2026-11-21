package ca.umanitoba.cs.pariyary.model;

import ca.umanitoba.cs.pariyary.exceptions.InvalidActivityTypeException;

public enum ActivityType {
    WALK,
    RUN,
    BIKE;

    public static ActivityType fromString(String activityType) throws InvalidActivityTypeException {
        return switch(activityType.trim().toLowerCase()) {
            case "walk" -> WALK;
            case "run" -> RUN;
            case "bike" -> BIKE;
            default -> throw new InvalidActivityTypeException();
        };
    }
}
