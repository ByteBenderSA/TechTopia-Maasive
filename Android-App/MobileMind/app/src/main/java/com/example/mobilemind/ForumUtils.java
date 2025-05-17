package com.example.mobilemind;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * Utility class for forum post formatting
 */
public class ForumUtils {

    /**
     * Format timestamp to show time ago (e.g. "5m ago", "2h ago", "3d ago")
     *
     * @param context Application context
     * @param timestamp Timestamp in milliseconds
     * @return Formatted string
     */
    public static String formatTimeAgo(Context context, long timestamp) {
        long now = System.currentTimeMillis();
        long difference = now - timestamp;

        // For very recent posts (less than a minute)
        if (difference < DateUtils.MINUTE_IN_MILLIS) {
            return "Just now";
        }

        // For posts between 1 minute and 24 hours
        if (difference < DateUtils.DAY_IN_MILLIS) {
            // Format as "X{m|h} ago" (e.g. "5m ago" or "2h ago")
            boolean isMinutes = difference < DateUtils.HOUR_IN_MILLIS;
            long value = isMinutes
                    ? difference / DateUtils.MINUTE_IN_MILLIS
                    : difference / DateUtils.HOUR_IN_MILLIS;
            String unit = isMinutes ? "m" : "h";
            return "Posted " + value + unit + " ago";
        }

        // For older posts
        return "Posted " + DateUtils.getRelativeTimeSpanString(timestamp, now,
                DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    /**
     * Extract initials from a user's name (up to 2 characters)
     *
     * @param name Full name of the user
     * @return One or two character initials
     */
    public static String getUserInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }

        StringBuilder initials = new StringBuilder();
        String[] nameParts = name.split("\\s+");

        for (int i = 0; i < Math.min(2, nameParts.length); i++) {
            if (nameParts[i].length() > 0) {
                initials.append(Character.toUpperCase(nameParts[i].charAt(0)));
            }
        }

        return initials.toString();
    }

    /**
     * Format large numbers for display (e.g. 1.2K instead of 1200)
     *
     * @param count The number to format
     * @return Formatted string
     */
    public static String formatCount(int count) {
        if (count < 1000) {
            return String.valueOf(count);
        } else if (count < 1000000) {
            return String.format("%.1fK", count / 1000.0f).replace(".0K", "K");
        } else {
            return String.format("%.1fM", count / 1000000.0f).replace(".0M", "M");
        }
    }
}