package com.example.mobilemind;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * VoteManagerHTTP handles all voting operations via HTTP requests to PHP backend
 * using POST form data (same as login system) with proper duplicate prevention
 */
public class VoteManagerHTTP {
    private static final String TAG = "VoteManagerHTTP";
    private Context context;
    
    // Update this URL to match your server setup (same as login.php)
    private static final String BASE_URL = "https://lamp.ms.wits.ac.za/home/s2688828/";
    private static final String VOTE_ENDPOINT = BASE_URL + "vote.php";
    private static final String GET_VOTE_STATUS_ENDPOINT = BASE_URL + "get_vote_status.php";

    public VoteManagerHTTP(Context context) {
        this.context = context;
    }

    /**
     * Toggle vote on a post - prevents duplicate voting
     * @param studentNumber Current user's student number
     * @param postId Post ID to vote on
     * @param callback Callback to handle the result
     */
    public void togglePostVote(String studentNumber, int postId, VoteCallback callback) {
        new VoteTask(callback).execute("post", studentNumber, String.valueOf(postId), null);
    }

    /**
     * Toggle vote on a comment - prevents duplicate voting
     * @param studentNumber Current user's student number
     * @param commentId Comment ID to vote on
     * @param callback Callback to handle the result
     */
    public void toggleCommentVote(String studentNumber, int commentId, VoteCallback callback) {
        new VoteTask(callback).execute("comment", studentNumber, null, String.valueOf(commentId));
    }

    /**
     * Check if user has voted on a post (for UI state)
     * @param studentNumber Current user's student number
     * @param postId Post ID to check
     * @param callback Callback to handle the result
     */
    public void checkPostVoteStatus(String studentNumber, int postId, VoteStatusCallback callback) {
        new VoteStatusTask(callback).execute("post", studentNumber, String.valueOf(postId), null);
    }

    /**
     * Check if user has voted on a comment (for UI state)
     * @param studentNumber Current user's student number
     * @param commentId Comment ID to check
     * @param callback Callback to handle the result
     */
    public void checkCommentVoteStatus(String studentNumber, int commentId, VoteStatusCallback callback) {
        new VoteStatusTask(callback).execute("comment", studentNumber, null, String.valueOf(commentId));
    }

    /**
     * Generic method to get vote status (for compatibility)
     * @param studentNumber Current user's student number
     * @param itemId Item ID (post or comment)
     * @param isComment true if checking comment vote, false if checking post vote
     * @param callback Callback to handle the result
     */
    public void getVoteStatus(String studentNumber, String itemId, boolean isComment, VoteStatusCallback callback) {
        if (isComment) {
            checkCommentVoteStatus(studentNumber, Integer.parseInt(itemId), callback);
        } else {
            checkPostVoteStatus(studentNumber, Integer.parseInt(itemId), callback);
        }
    }

    /**
     * AsyncTask for voting operations - using POST form data (same as login)
     */
    private class VoteTask extends AsyncTask<String, Void, VoteResult> {
        private VoteCallback callback;

        public VoteTask(VoteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected VoteResult doInBackground(String... params) {
            String voteType = params[0];
            String studentNumber = params[1];
            String postId = params[2];
            String commentId = params[3];

            try {
                URL url = new URL(VOTE_ENDPOINT);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                // Create form data (same as login system)
                StringBuilder postData = new StringBuilder();
                postData.append("STUDENT_NUMBER=").append(URLEncoder.encode(studentNumber, "UTF-8"));
                postData.append("&VOTE_TYPE=").append(URLEncoder.encode(voteType, "UTF-8"));
                
                if ("post".equals(voteType)) {
                    postData.append("&POST_ID=").append(URLEncoder.encode(postId, "UTF-8"));
                } else {
                    postData.append("&COMMENT_ID=").append(URLEncoder.encode(commentId, "UTF-8"));
                }

                // Send request
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(postData.toString());
                writer.flush();
                writer.close();
                os.close();

                // Read response
                int responseCode = connection.getResponseCode();
                InputStream inputStream = responseCode >= 200 && responseCode < 300 
                    ? connection.getInputStream() 
                    : connection.getErrorStream();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse response
                JSONObject jsonResponse = new JSONObject(response.toString());
                
                if (jsonResponse.optBoolean("success", false)) {
                    return new VoteResult(
                        true,
                        jsonResponse.optInt("new_vote_count", 0),
                        jsonResponse.optString("message", "Vote processed successfully")
                    );
                } else {
                    return new VoteResult(
                        false,
                        0,
                        jsonResponse.optString("message", "Vote failed")
                    );
                }

            } catch (Exception e) {
                Log.e(TAG, "Error in vote request", e);
                return new VoteResult(false, 0, "Network error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(VoteResult result) {
            if (callback != null) {
                callback.onVoteResult(result);
            }
        }
    }

    /**
     * AsyncTask for checking vote status - using POST form data (same as login)
     */
    private class VoteStatusTask extends AsyncTask<String, Void, VoteStatusResult> {
        private VoteStatusCallback callback;

        public VoteStatusTask(VoteStatusCallback callback) {
            this.callback = callback;
        }

        @Override
        protected VoteStatusResult doInBackground(String... params) {
            String voteType = params[0];
            String studentNumber = params[1];
            String postId = params[2];
            String commentId = params[3];

            try {
                URL url = new URL(GET_VOTE_STATUS_ENDPOINT);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                // Create form data (same as login system)
                StringBuilder postData = new StringBuilder();
                postData.append("STUDENT_NUMBER=").append(URLEncoder.encode(studentNumber, "UTF-8"));
                postData.append("&VOTE_TYPE=").append(URLEncoder.encode(voteType, "UTF-8"));
                
                if ("post".equals(voteType)) {
                    postData.append("&POST_ID=").append(URLEncoder.encode(postId, "UTF-8"));
                } else {
                    postData.append("&COMMENT_ID=").append(URLEncoder.encode(commentId, "UTF-8"));
                }

                // Send request
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(postData.toString());
                writer.flush();
                writer.close();
                os.close();

                // Read response
                int responseCode = connection.getResponseCode();
                InputStream inputStream = responseCode >= 200 && responseCode < 300 
                    ? connection.getInputStream() 
                    : connection.getErrorStream();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse response
                JSONObject jsonResponse = new JSONObject(response.toString());
                
                if (jsonResponse.optBoolean("success", false)) {
                    return new VoteStatusResult(
                        true,
                        jsonResponse.optBoolean("has_voted", false),
                        jsonResponse.optInt("vote_count", 0)
                    );
                } else {
                    return new VoteStatusResult(
                        false,
                        false,
                        0
                    );
                }

            } catch (Exception e) {
                Log.e(TAG, "Error in vote status request", e);
                return new VoteStatusResult(false, false, 0);
            }
        }

        @Override
        protected void onPostExecute(VoteStatusResult result) {
            if (callback != null) {
                callback.onVoteStatusResult(result);
            }
        }
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

    /**
     * Result class for vote status checks
     */
    public static class VoteStatusResult {
        private boolean success;
        private boolean hasVoted;
        private int voteCount;

        public VoteStatusResult(boolean success, boolean hasVoted, int voteCount) {
            this.success = success;
            this.hasVoted = hasVoted;
            this.voteCount = voteCount;
        }

        public boolean isSuccess() { return success; }
        public boolean hasVoted() { return hasVoted; }
        public int getVoteCount() { return voteCount; }
        
        // Compatibility method - returns 1 if voted, 0 if not voted
        public int getVoteStatus() {
            return hasVoted ? 1 : 0;
        }
    }

    /**
     * Callback interface for vote operations
     */
    public interface VoteCallback {
        void onVoteResult(VoteResult result);
    }

    /**
     * Callback interface for vote status checks
     */
    public interface VoteStatusCallback {
        void onVoteStatusResult(VoteStatusResult result);
    }
} 