package ca.umanitoba.cs.pariyary.logic;
import ca.umanitoba.cs.pariyary.model.Coordinates;
import ca.umanitoba.cs.pariyary.model.User;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathManager {

    private final SessionManager sessionManager;
    private final FeedManager feedManager;
    private final PathFinder pathFinder;

    public PathManager(SessionManager sessionManager,
                       FeedManager feedManager,
                       PathFinder pathFinder) {
        this.sessionManager = sessionManager;
        this.feedManager = feedManager;
        this.pathFinder = pathFinder;

        checkPathManager();
    }

    public List<Coordinates> findPathUsingOwnRoutes(Coordinates start, Coordinates end) {

        checkPathManager();
        Preconditions.checkNotNull(start);
        Preconditions.checkNotNull(end);

        User currUser = sessionManager.getCurrUser();
        Collection<Coordinates> allowedCoordinates =
                currUser.getActivitiesOccupiedCoords();

        return pathFinder.findPath(start, end, allowedCoordinates);
    }

    public List<Coordinates> findPathUsingFeedRoutes(Coordinates start, Coordinates end) {

        checkPathManager();
        Preconditions.checkNotNull(start);
        Preconditions.checkNotNull(end);

        Set<Coordinates> allowedCoordinates = new HashSet<>();
        List<FeedManager.FeedEntry> feed =
                feedManager.getFeed(sessionManager.getCurrUser());

        for (FeedManager.FeedEntry entry : feed) {
            allowedCoordinates.addAll(entry.activity().getRoute());
        }

        return pathFinder.findPath(start, end, allowedCoordinates);
    }

    private void checkPathManager() {
        Preconditions.checkNotNull(sessionManager);
        Preconditions.checkNotNull(feedManager);
    }
}