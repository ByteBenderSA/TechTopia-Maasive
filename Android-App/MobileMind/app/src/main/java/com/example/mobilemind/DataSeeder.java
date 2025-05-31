package com.example.mobilemind;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DataSeeder class to populate SharedPreferences with sample data
 * that matches the database schema for testing purposes
 */
public class DataSeeder {
    private static final String TAG = "DataSeeder";
    private Context context;
    private SharedPreferences sharedPreferences;

    public DataSeeder(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    /**
     * Seed all sample data (posts, comments, votes)
     */
    public void seedAllData() {
        try {
            seedPosts();
            seedComments();
            seedVotes();
            Log.d(TAG, "All sample data seeded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error seeding data", e);
        }
    }

    /**
     * Seed sample posts data
     */
    private void seedPosts() throws JSONException {
        JSONArray postsArray = new JSONArray();

        // Calculate recent timestamps for realistic "time ago" display
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        
        // Post 1 - 5 minutes ago
        String post1Date = dateFormat.format(new Date(currentTime - 300000));
        // Post 2 - 45 minutes ago  
        String post2Date = dateFormat.format(new Date(currentTime - 2700000));
        // Post 3 - 2 hours ago
        String post3Date = dateFormat.format(new Date(currentTime - 7200000));

        // Post 1
        JSONObject post1 = new JSONObject();
        post1.put("POST_ID", 1);
        post1.put("STUDENT_NUMBER", "2688828");
        post1.put("AUTHOR_NAME", "John Doe");
        post1.put("TITLE", "Help with Android Studio Setup");
        post1.put("POST_QUESTION", "Hi everyone! I am having trouble setting up Android Studio on my laptop. It keeps crashing when I try to create a new project. Has anyone experienced this issue before?");
        post1.put("POST_DATE", post1Date);
        post1.put("VOTE_COUNT", 3);
        post1.put("STATUS", 1);
        postsArray.put(post1);

        // Post 2
        JSONObject post2 = new JSONObject();
        post2.put("POST_ID", 2);
        post2.put("STUDENT_NUMBER", "2666605");
        post2.put("AUTHOR_NAME", "Sarah Smith");
        post2.put("TITLE", "Study Group for Database Assignment");
        post2.put("POST_QUESTION", "Looking to form a study group for the upcoming database assignment. We need to design a complete database schema for a mobile app. Anyone interested?");
        post2.put("POST_DATE", post2Date);
        post2.put("VOTE_COUNT", 3);
        post2.put("STATUS", 1);
        postsArray.put(post2);

        // Post 3
        JSONObject post3 = new JSONObject();
        post3.put("POST_ID", 3);
        post3.put("STUDENT_NUMBER", "2548941");
        post3.put("AUTHOR_NAME", "Mike Johnson");
        post3.put("TITLE", "Question about RecyclerView Implementation");
        post3.put("POST_QUESTION", "I am working on implementing a RecyclerView in my Android app but the data is not displaying correctly. The list appears empty even though I have added items to the adapter.");
        post3.put("POST_DATE", post3Date);
        post3.put("VOTE_COUNT", 3);
        post3.put("STATUS", 1);
        postsArray.put(post3);

        // Save posts data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("posts_data", postsArray.toString());
        editor.apply();
        
        Log.d(TAG, "Posts data seeded: " + postsArray.length() + " posts");
    }

    /**
     * Seed sample comments data
     */
    private void seedComments() throws JSONException {
        JSONArray commentsArray = new JSONArray();

        // Calculate recent timestamps for realistic "time ago" display
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Comments for Post 1 (Android Studio Setup) - replies to 5m ago post
        JSONObject comment1 = new JSONObject();
        comment1.put("COMMENT_ID", 1);
        comment1.put("POST_ID", 1);
        comment1.put("STUDENT_NUMBER", "2666605");
        comment1.put("AUTHOR_NAME", "Sarah Smith");
        comment1.put("STUDENT_COMMENT", "I had the same issue! Try clearing the Android Studio cache and restarting. Go to File > Invalidate Caches and Restart.");
        comment1.put("COMMENT_DATE", dateFormat.format(new Date(currentTime - 240000))); // 4 minutes ago
        comment1.put("VOTE_COUNT", 2);
        comment1.put("STATUS", 1);
        commentsArray.put(comment1);

        JSONObject comment2 = new JSONObject();
        comment2.put("COMMENT_ID", 2);
        comment2.put("POST_ID", 1);
        comment2.put("STUDENT_NUMBER", "2548941");
        comment2.put("AUTHOR_NAME", "Mike Johnson");
        comment2.put("STUDENT_COMMENT", "Also make sure you have enough RAM allocated to Android Studio. Check your studio.exe.vmoptions file.");
        comment2.put("COMMENT_DATE", dateFormat.format(new Date(currentTime - 180000))); // 3 minutes ago
        comment2.put("VOTE_COUNT", 1);
        comment2.put("STATUS", 1);
        commentsArray.put(comment2);

        // Comments for Post 2 (Study Group) - replies to 45m ago post
        JSONObject comment3 = new JSONObject();
        comment3.put("COMMENT_ID", 3);
        comment3.put("POST_ID", 2);
        comment3.put("STUDENT_NUMBER", "2688828");
        comment3.put("AUTHOR_NAME", "John Doe");
        comment3.put("STUDENT_COMMENT", "I would be interested in joining! I am also working on the database assignment. When and where should we meet?");
        comment3.put("COMMENT_DATE", dateFormat.format(new Date(currentTime - 2400000))); // 40 minutes ago
        comment3.put("VOTE_COUNT", 0);
        comment3.put("STATUS", 1);
        commentsArray.put(comment3);

        JSONObject comment4 = new JSONObject();
        comment4.put("COMMENT_ID", 4);
        comment4.put("POST_ID", 2);
        comment4.put("STUDENT_NUMBER", "2548941");
        comment4.put("AUTHOR_NAME", "Mike Johnson");
        comment4.put("STUDENT_COMMENT", "Count me in as well. I have some experience with database design from previous courses.");
        comment4.put("COMMENT_DATE", dateFormat.format(new Date(currentTime - 2100000))); // 35 minutes ago
        comment4.put("VOTE_COUNT", 1);
        comment4.put("STATUS", 1);
        commentsArray.put(comment4);

        // Comments for Post 3 (RecyclerView) - replies to 2h ago post
        JSONObject comment5 = new JSONObject();
        comment5.put("COMMENT_ID", 5);
        comment5.put("POST_ID", 3);
        comment5.put("STUDENT_NUMBER", "2688828");
        comment5.put("AUTHOR_NAME", "John Doe");
        comment5.put("STUDENT_COMMENT", "Check if you are calling notifyDataSetChanged() after adding items to your adapter.");
        comment5.put("COMMENT_DATE", dateFormat.format(new Date(currentTime - 6900000))); // 1h 55m ago
        comment5.put("VOTE_COUNT", 1);
        comment5.put("STATUS", 1);
        commentsArray.put(comment5);

        JSONObject comment6 = new JSONObject();
        comment6.put("COMMENT_ID", 6);
        comment6.put("POST_ID", 3);
        comment6.put("STUDENT_NUMBER", "2666605");
        comment6.put("AUTHOR_NAME", "Sarah Smith");
        comment6.put("STUDENT_COMMENT", "Also make sure your RecyclerView layout manager is properly set. LinearLayoutManager is most common.");
        comment6.put("COMMENT_DATE", dateFormat.format(new Date(currentTime - 6600000))); // 1h 50m ago
        comment6.put("VOTE_COUNT", 1);
        comment6.put("STATUS", 1);
        commentsArray.put(comment6);

        // Save comments data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("comments_data", commentsArray.toString());
        editor.apply();
        
        Log.d(TAG, "Comments data seeded: " + commentsArray.length() + " comments");
    }

    /**
     * Seed sample votes data
     */
    private void seedVotes() throws JSONException {
        JSONArray votesArray = new JSONArray();

        // Post votes (following database constraints: one vote per user per post)
        JSONObject vote1 = new JSONObject();
        vote1.put("VOTE_ID", 1);
        vote1.put("STUDENT_NUMBER", "2666605");
        vote1.put("POST_ID", 1);
        vote1.put("COMMENT_ID", JSONObject.NULL);
        vote1.put("VOTE_TYPE", 1);
        vote1.put("VOTED_DATE", "2024-01-15 14:31:00");
        votesArray.put(vote1);

        JSONObject vote2 = new JSONObject();
        vote2.put("VOTE_ID", 2);
        vote2.put("STUDENT_NUMBER", "2548941");
        vote2.put("POST_ID", 1);
        vote2.put("COMMENT_ID", JSONObject.NULL);
        vote2.put("VOTE_TYPE", 1);
        vote2.put("VOTED_DATE", "2024-01-15 14:32:00");
        votesArray.put(vote2);

        JSONObject vote3 = new JSONObject();
        vote3.put("VOTE_ID", 3);
        vote3.put("STUDENT_NUMBER", "2729013");
        vote3.put("POST_ID", 1);
        vote3.put("COMMENT_ID", JSONObject.NULL);
        vote3.put("VOTE_TYPE", 1);
        vote3.put("VOTED_DATE", "2024-01-15 14:33:00");
        votesArray.put(vote3);

        // Comment votes (following database constraints: one vote per user per comment)
        JSONObject vote4 = new JSONObject();
        vote4.put("VOTE_ID", 4);
        vote4.put("STUDENT_NUMBER", "2548941");
        vote4.put("POST_ID", JSONObject.NULL);
        vote4.put("COMMENT_ID", 1);
        vote4.put("VOTE_TYPE", 1);
        vote4.put("VOTED_DATE", "2024-01-15 14:36:00");
        votesArray.put(vote4);

        JSONObject vote5 = new JSONObject();
        vote5.put("VOTE_ID", 5);
        vote5.put("STUDENT_NUMBER", "2729013");
        vote5.put("POST_ID", JSONObject.NULL);
        vote5.put("COMMENT_ID", 1);
        vote5.put("VOTE_TYPE", 1);
        vote5.put("VOTED_DATE", "2024-01-15 14:37:00");
        votesArray.put(vote5);

        JSONObject vote6 = new JSONObject();
        vote6.put("VOTE_ID", 6);
        vote6.put("STUDENT_NUMBER", "2688828");
        vote6.put("POST_ID", JSONObject.NULL);
        vote6.put("COMMENT_ID", 2);
        vote6.put("VOTE_TYPE", 1);
        vote6.put("VOTED_DATE", "2024-01-15 14:41:00");
        votesArray.put(vote6);

        // Save votes data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("votes_data", votesArray.toString());
        editor.apply();
        
        Log.d(TAG, "Votes data seeded: " + votesArray.length() + " votes");
    }

    /**
     * Clear all seeded data
     */
    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("posts_data");
        editor.remove("comments_data");
        editor.remove("votes_data");
        editor.apply();
        
        Log.d(TAG, "All seeded data cleared");
    }

    /**
     * Check if data has been seeded already
     */
    public boolean isDataSeeded() {
        String postsData = sharedPreferences.getString("posts_data", "");
        return !postsData.isEmpty();
    }
}