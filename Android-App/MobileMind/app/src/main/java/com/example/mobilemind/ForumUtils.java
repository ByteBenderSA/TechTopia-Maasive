package com.example.mobilemind;

/**
 * Simple utility class for common functions
 */
public class ForumUtils {

    /**
     * Get user initials from name
     */
    public static String getUserInitials(String name) {
        if (name == null || name.isEmpty()) {
            return "?";
        }
        
        String[] parts = name.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
        } else {
            return String.valueOf(name.charAt(0)).toUpperCase();
        }
    }

    /**
     * Format timestamp to relative time (e.g., "5m ago", "2h ago", "3d ago")
     */
    public static String formatTimeAgo(android.content.Context context, long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;
        
        // Convert to different time units
        long seconds = timeDifference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;
        
        if (seconds < 60) {
            return "just now";
        } else if (minutes < 60) {
            return minutes + "m ago";
        } else if (hours < 24) {
            return hours + "h ago";
        } else if (days < 7) {
            return days + "d ago";
        } else if (weeks < 4) {
            return weeks + "w ago";
        } else if (months < 12) {
            return months + " month" + (months > 1 ? "s" : "") + " ago";
        } else {
            return years + " year" + (years > 1 ? "s" : "") + " ago";
        }
    }

    /**
     * Format numbers (simple version)
     */
    public static String formatCount(int count) {
        return String.valueOf(count);
    }
}