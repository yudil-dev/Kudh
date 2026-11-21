package ca.umanitoba.cs.pariyary.logic;

import ca.umanitoba.cs.pariyary.model.Activity;
import ca.umanitoba.cs.pariyary.model.User;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class FeedManager {

    public List<FeedEntry> getFeed(User user) {

        Preconditions.checkNotNull(user, "User cannot be null");

        List<FeedEntry> feed = new ArrayList<>();
        for (Activity activity : user.getHistoryActivity().values()) {
            feed.add(new FeedEntry(user, activity));
        }

        for (User followedUser : user.getFollowedUsers()) {
            for (Activity activity : followedUser.getHistoryActivity().values()) {
                feed.add(new FeedEntry(followedUser, activity));
            }
        }

        feed.sort(Comparator.comparing(
                (FeedEntry entry) -> entry.activity().getStartDateTime()
        ).reversed());

        return feed;
    }

    public record FeedEntry(User user, Activity activity) {
    }
}
