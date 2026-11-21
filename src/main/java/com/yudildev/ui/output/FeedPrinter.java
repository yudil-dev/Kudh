package com.yudildev.ui.output;

import com.yudildev.logic.FeedManager;
import com.yudildev.model.Map;


import java.util.Collection;

public class FeedPrinter {

    private final Map world;
    private final MapPrinter mapPrinter;

    public FeedPrinter(Map world, MapPrinter mapActivityPrinter) {
        this.world = world;
        this.mapPrinter = mapActivityPrinter;
    }

    public void print(Collection<FeedManager.FeedEntry> feed) {

        if (feed == null || feed.isEmpty()) {
            System.out.println("No activities in your feed yet.");
            return;
        }

        System.out.println("\n--- Your Feed ---");

        for (FeedManager.FeedEntry entry : feed) {
            FeedEntryPrinter.print(entry);
            mapPrinter.printMap(world, entry.activity().getRoute(), null);
        }
    }
}