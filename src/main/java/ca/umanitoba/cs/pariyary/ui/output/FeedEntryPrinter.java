package ca.umanitoba.cs.pariyary.ui.output;

import ca.umanitoba.cs.pariyary.logic.FeedManager;

public class FeedEntryPrinter {

    public static void print(FeedManager.FeedEntry entry) {
        System.out.println(entry.user().getUsername()
                + " | "
                + entry.activity().getType()
                + " | "
                + entry.activity().getStartDateTime().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));;
    }
}
