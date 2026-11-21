package com.yudildev.ui;

import com.yudildev.logic.FeedManager.FeedEntry;
import com.yudildev.ui.output.FeedPrinter;

import java.util.List;
import java.util.Scanner;

public class MainMenuDisplay {

    private final Scanner input;
    private final FeedPrinter feedPrinter;

    public MainMenuDisplay(Scanner input, FeedPrinter feedPrinter) {
        this.input = input;
        this.feedPrinter = feedPrinter;
    }

    public String getChoice(List<FeedEntry> feed) {

        feedPrinter.print(feed);

        System.out.println("\nChoose an option:");
        System.out.println("1. Update Account");
        System.out.println("2. Follow Someone");
        System.out.println("3. Add Activity");
        System.out.println("4. Find Path");
        System.out.println("5. Logout");

        System.out.print("Enter choice (1 - 5): ");
        return input.nextLine();
    }
}