package com.yudildev;

import com.yudildev.logic.*;
import com.yudildev.model.ExerciseTracker;
import com.yudildev.model.Map;
import com.yudildev.ui.*;
import com.yudildev.ui.output.FeedPrinter;
import com.yudildev.ui.output.MapPrinter;

import java.util.Scanner;

public class TrackerMain {
    public static void main(String[] args) {

        ExerciseTracker et = new ExerciseTracker(new Map(10, 10));
        SessionManager sm = new SessionManager(et);
        FeedManager fm = new FeedManager();
        ActivityManager am = new ActivityManager(sm);
        FollowManager fmf = new FollowManager(sm);
        AccountManager accm = new AccountManager(sm);
        PathManager pm = new PathManager(sm, fm,
                new PathFinder(sm.getTracker().getMap()));
        Scanner sc = new Scanner(System.in);


        ExerciseTrackerAppDisplay app = new ExerciseTrackerAppDisplay(
                new ExerciseTrackerApp(sm, fm, am, fmf, accm, pm),
                new LoginDisplay(sc, sm),
                new LoggedOutMenuDisplay(sc),
                new CreateUserDisplay(sc, sm),
                new MainMenuDisplay(sc, new FeedPrinter(sm.getTracker().getMap(), new MapPrinter())),
                new AddActivityDisplay(sc, sm,
                        new ActivityCreation(sm.getTracker()),
                        new ObstacleCreation(sm.getTracker()), am),
                new FollowDisplay(sc, fmf),
                new UpdateAccountDisplay(sc, accm, sm),
                new PathFinderDisplay(sc, pm, sm.getTracker().getMap())
        );

        app.run();
    }
}
