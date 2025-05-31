package com.example.mobilemind;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * VoteManager handles all voting operations with proper duplicate prevention
 * following the database schema constraints
 */
public class VoteManager {
    private static final String TAG = "VoteManager";
    private Context context;
    private SharedPreferences sharedPreferences;

    // Vote types from database schema
    private static final int VOTE_TYPE_NONE = 0;
    private static final int VOTE_TYPE_UPVOTE = 1;

    public VoteManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    /**
     * Toggle vote on a post - prevents duplicate voting
     * @param studentNumber Current user's student number
     * @param postId Post ID to vote on
     * @return VoteResult containing success status and new vote count
     */
    public VoteResult togglePostVote(String studentNumber, int postId) {
        try {
            // Check if user already voted on this post
            boolean hasExistingVote = hasUserVotedOnPost(studentNumber, postId);
            
            if (hasExistingVote) {
                // Remove existing vote
                return removePostVote(studentNumber, postId);
            } else {
                // Add new vote
                return addPostVote(studentNumber, postId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error toggling post vote", e);
            return new VoteResult(false, 0, "Error: " + e.getMessage());
        }
    }

    /**
     * Toggle vote on a comment - prevents duplicate voting
     * @param studentNumber Current user's student number
     * @param commentId Comment ID to vote on
     * @return VoteResult containing success status and new vote count
     */
    public VoteResult toggleCommentVote(String studentNumber, int commentId) {
        try {
            // Check if user already voted on this comment
            boolean hasExistingVote = hasUserVotedOnComment(studentNumber, commentId);
            
            if (hasExistingVote) {
                // Remove existing vote
                return removeCommentVote(studentNumber, commentId);
            } else {
                // Add new vote
                return addCommentVote(studentNumber, commentId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error toggling comment vote", e);
            return new VoteResult(false, 0, "Error: " + e.getMessage());
        }
    }

    /**
     * Check if user has already voted on a specific post
     */
    private boolean hasUserVotedOnPost(String studentNumber, int postId) {
        try {
            String votesData = sharedPreferences.getString("votes_data", "[]");
            JSONArray votesArray = new JSONArray(votesData);
            
            for (int i = 0; i < votesArray.length(); i++) {
                JSONObject vote = votesArray.getJSONObject(i);
                
                // Check for post vote (POST_ID not null, COMMENT_ID null)
                if (vote.getString("STUDENT_NUMBER").equals(studentNumber) &&
                    !vote.isNull("POST_ID") &&
                    vote.getInt("POST_ID") == postId &&
                    vote.isNull("COMMENT_ID") &&
                    vote.getInt("VOTE_TYPE") == VOTE_TYPE_UPVOTE) {
                    return true;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error checking existing post vote", e);
        }
        return false;
    }

    /**
     * Check if user has already voted on a specific comment
     */
    private boolean hasUserVotedOnComment(String studentNumber, int commentId) {
        try {
            String votesData = sharedPreferences.getString("votes_data", "[]");
            JSONArray votesArray = new JSONArray(votesData);
            
            for (int i = 0; i < votesArray.length(); i++) {
                JSONObject vote = votesArray.getJSONObject(i);
                
                // Check for comment vote (COMMENT_ID not null, POST_ID null)
                if (vote.getString("STUDENT_NUMBER").equals(studentNumber) &&
                    vote.isNull("POST_ID") &&
                    !vote.isNull("COMMENT_ID") &&
                    vote.getInt("COMMENT_ID") == commentId &&
                    vote.getInt("VOTE_TYPE") == VOTE_TYPE_UPVOTE) {
                    return true;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error checking existing comment vote", e);
        }
        return false;
    }

    /**
     * Add a new post vote
     */
    private VoteResult addPostVote(String studentNumber, int postId) {
        try {
            // Add vote record
            String votesData = sharedPreferences.getString("votes_data", "[]");
            JSONArray votesArray = new JSONArray(votesData);
            
            // Create new vote record
            JSONObject newVote = new JSONObject();
            newVote.put("VOTE_ID", getNextVoteId(votesArray));
            newVote.put("STUDENT_NUMBER", studentNumber);
            newVote.put("POST_ID", postId);
            newVote.put("COMMENT_ID", JSONObject.NULL);
            newVote.put("VOTE_TYPE", VOTE_TYPE_UPVOTE);
            newVote.put("VOTED_DATE", getCurrentTimestamp());
            
            votesArray.put(newVote);
            
            // Save votes data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("votes_data", votesArray.toString());
            editor.apply();
            
            // Update post vote count
            int newVoteCount = updatePostVoteCount(postId, 1);
            
            return new VoteResult(true, newVoteCount, "Vote added successfully");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error adding post vote", e);
            return new VoteResult(false, 0, "Error adding vote");
        }
    }

    /**
     * Remove a post vote
     */
    private VoteResult removePostVote(String studentNumber, int postId) {
        try {
            String votesData = sharedPreferences.getString("votes_data", "[]");
            JSONArray votesArray = new JSONArray(votesData);
            JSONArray newVotesArray = new JSONArray();
            
            // Remove the specific vote
            for (int i = 0; i < votesArray.length(); i++) {
                JSONObject vote = votesArray.getJSONObject(i);
                
                // Skip the vote we want to remove
                if (vote.getString("STUDENT_NUMBER").equals(studentNumber) &&
                    !vote.isNull("POST_ID") &&
                    vote.getInt("POST_ID") == postId &&
                    vote.isNull("COMMENT_ID")) {
                    continue; // Skip this vote (remove it)
                }
                
                newVotesArray.put(vote);
            }
            
            // Save updated votes data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("votes_data", newVotesArray.toString());
            editor.apply();
            
            // Update post vote count
            int newVoteCount = updatePostVoteCount(postId, -1);
            
            return new VoteResult(true, newVoteCount, "Vote removed successfully");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error removing post vote", e);
            return new VoteResult(false, 0, "Error removing vote");
        }
    }

    /**
     * Add a new comment vote
     */
    private VoteResult addCommentVote(String studentNumber, int commentId) {
        try {
            // Add vote record
            String votesData = sharedPreferences.getString("votes_data", "[]");
            JSONArray votesArray = new JSONArray(votesData);
            
            // Create new vote record
            JSONObject newVote = new JSONObject();
            newVote.put("VOTE_ID", getNextVoteId(votesArray));
            newVote.put("STUDENT_NUMBER", studentNumber);
            newVote.put("POST_ID", JSONObject.NULL);
            newVote.put("COMMENT_ID", commentId);
            newVote.put("VOTE_TYPE", VOTE_TYPE_UPVOTE);
            newVote.put("VOTED_DATE", getCurrentTimestamp());
            
            votesArray.put(newVote);
            
            // Save votes data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("votes_data", votesArray.toString());
            editor.apply();
            
            // Update comment vote count
            int newVoteCount = updateCommentVoteCount(commentId, 1);
            
            return new VoteResult(true, newVoteCount, "Vote added successfully");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error adding comment vote", e);
            return new VoteResult(false, 0, "Error adding vote");
        }
    }

    /**
     * Remove a comment vote
     */
    private VoteResult removeCommentVote(String studentNumber, int commentId) {
        try {
            String votesData = sharedPreferences.getString("votes_data", "[]");
            JSONArray votesArray = new JSONArray(votesData);
            JSONArray newVotesArray = new JSONArray();
            
            // Remove the specific vote
            for (int i = 0; i < votesArray.length(); i++) {
                JSONObject vote = votesArray.getJSONObject(i);
                
                // Skip the vote we want to remove
                if (vote.getString("STUDENT_NUMBER").equals(studentNumber) &&
                    vote.isNull("POST_ID") &&
                    !vote.isNull("COMMENT_ID") &&
                    vote.getInt("COMMENT_ID") == commentId) {
                    continue; // Skip this vote (remove it)
                }
                
                newVotesArray.put(vote);
            }
            
            // Save updated votes data
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("votes_data", newVotesArray.toString());
            editor.apply();
            
            // Update comment vote count
            int newVoteCount = updateCommentVoteCount(commentId, -1);
            
            return new VoteResult(true, newVoteCount, "Vote removed successfully");
            
        } catch (JSONException e) {
            Log.e(TAG, "Error removing comment vote", e);
            return new VoteResult(false, 0, "Error removing vote");
        }
    }

    /**
     * Update post vote count in posts data
     */
    private int updatePostVoteCount(int postId, int delta) {
        try {
            String postsData = sharedPreferences.getString("posts_data", "[]");
            JSONArray postsArray = new JSONArray(postsData);
            
            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject post = postsArray.getJSONObject(i);
                if (post.getInt("POST_ID") == postId) {
                    int currentVotes = post.getInt("VOTE_COUNT");
                    int newVotes = Math.max(0, currentVotes + delta); // Prevent negative votes
                    post.put("VOTE_COUNT", newVotes);
                    
                    // Save updated posts data
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("posts_data", postsArray.toString());
                    editor.apply();
                    
                    return newVotes;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error updating post vote count", e);
        }
        return 0;
    }

    /**
     * Update comment vote count in comments data
     */
    private int updateCommentVoteCount(int commentId, int delta) {
        try {
            String commentsData = sharedPreferences.getString("comments_data", "[]");
            JSONArray commentsArray = new JSONArray(commentsData);
            
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject comment = commentsArray.getJSONObject(i);
                if (comment.getInt("COMMENT_ID") == commentId) {
                    int currentVotes = comment.getInt("VOTE_COUNT");
                    int newVotes = Math.max(0, currentVotes + delta); // Prevent negative votes
                    comment.put("VOTE_COUNT", newVotes);
                    
                    // Save updated comments data
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("comments_data", commentsArray.toString());
                    editor.apply();
                    
                    return newVotes;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error updating comment vote count", e);
        }
        return 0;
    }

    /**
     * Get next available vote ID
     */
    private int getNextVoteId(JSONArray votesArray) {
        int maxId = 0;
        try {
            for (int i = 0; i < votesArray.length(); i++) {
                JSONObject vote = votesArray.getJSONObject(i);
                int voteId = vote.getInt("VOTE_ID");
                maxId = Math.max(maxId, voteId);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error getting next vote ID", e);
        }
        return maxId + 1;
    }

    /**
     * Get current timestamp in database format
     */
    private String getCurrentTimestamp() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", 
                java.util.Locale.getDefault()).format(new java.util.Date());
    }

    /**
     * Check if user has voted on a post (for UI state)
     */
    public boolean hasUserVotedOnPost(String studentNumber, int postId, boolean returnValue) {
        return hasUserVotedOnPost(studentNumber, postId);
    }

    /**
     * Check if user has voted on a comment (for UI state)
     */
    public boolean hasUserVotedOnComment(String studentNumber, int commentId, boolean returnValue) {
        return hasUserVotedOnComment(studentNumber, commentId);
    }

    /**
     * Result class for vote operations
     */
    public static class VoteResult {
        private boolean success;
        private int newVoteCount;
        private String message;

        public VoteResult(boolean success, int newVoteCount, String message) {
            this.success = success;
            this.newVoteCount = newVoteCount;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public int getNewVoteCount() { return newVoteCount; }
        public String getMessage() { return message; }
    }
} 