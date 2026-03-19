package ca.umanitoba.cs.pariyary.output;

import ca.umanitoba.cs.pariyary.model.Activity;
import ca.umanitoba.cs.pariyary.model.User;

import java.time.LocalDateTime;

public class UserPrinter {

    public static void printSummaryUserActivity(User user) {

        int totalDistanceLifeTime = ActivitesDistanceTotal(user);
        int totalDistanceCurrWeek = DistanceTotalLast7Days(user);
        int totalDistanceCurrMonth = DistanceTotalLast30Days(user);


        System.out.println("Total distance of activities in life time is " + totalDistanceLifeTime + "kms");
        System.out.println("Total distance of activities in last 7 days is " + totalDistanceCurrWeek + "kms");
        System.out.println("Total distance of activities in last 30 days is " + totalDistanceCurrMonth + "kms");
    }

    private static int sumActivitiesSince(User user, LocalDateTime cutoff) {
        int total = 0;

        for (Activity activity : user.getHistoryActivity().values()) {
            if (cutoff == null || !activity.getStartDateTime().isBefore(cutoff)) {
                total += Math.max(0, activity.getRoute().size() - 1);
            }
        }

        return total;
    }

    public static int DistanceTotalLast7Days(User user) {
        return sumActivitiesSince(user, LocalDateTime.now().minusDays(7));
    }

    public static int DistanceTotalLast30Days(User user) {
        return sumActivitiesSince(user, LocalDateTime.now().minusMonths(1));
    }

    public static int ActivitesDistanceTotal(User user) {
        return sumActivitiesSince(user, null);
    }
}
