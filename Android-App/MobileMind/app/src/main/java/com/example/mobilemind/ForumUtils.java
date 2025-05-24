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
     * Simple time formatting
     */
    public static String formatTimeAgo(android.content.Context context, long timestamp) {
        // Just return a simple string for student project
        return "5m ago";
    }

    /**
     * Format numbers (simple version)
     */
    public static String formatCount(int count) {
        return String.valueOf(count);
    }
}