package com.yudildev.ui.output;

import com.yudildev.model.Activity;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ActivityPrinter {

    public static void print(Activity activity) {
        System.out.printf("""
                        Activity ID = %d
                        Type = %s
                        Start Date and Time = %s
                        End Date and Time = = %s
                        Route = %s
                        """,
                activity.getActivityID(),
                activity.getType(),
                activity.getStartDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                activity.getEndDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                activity.getRoute());
    }
}
