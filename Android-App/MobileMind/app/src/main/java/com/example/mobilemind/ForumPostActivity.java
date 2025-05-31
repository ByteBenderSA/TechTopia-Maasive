package com.example.mobilemind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Button;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumPostActivity extends AppCompatActivity {

    private TextView profileInitials;
    private TextView userName;
    private TextView postTime;
    private TextView postHeading;
    private TextView postBody;
    private TextView likesCount;
    private TextView commentsCount;
    private LinearLayout likesContainer;
    private LinearLayout commentsContainer;
    private ImageButton menuButton;

    // Comment section elements
    private RecyclerView commentsRecyclerView;
    private CommentAdapterHTTP commentAdapter;
    private List<Comment> commentsList;
    private EditText commentInput;
    private ImageButton postCommentButton;
    private TextView currentUserInitials;
    private TextView commentsEmptyState;

    // UPDATED: Using HTTP-based voting system instead of SharedPreferences
    private VoteManagerHTTP voteManagerHTTP;
    private String currentUserId = "user123";
    private String currentUserName = "Current User";
    private int currentPostId;

    // Mock data for testing
    private ForumPost currentPost;

    // New member variable for the post like icon
    private ImageView postLikeIcon;


    private List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        // Initialize sample data if needed (keeping for fallback)
        DataSeeder dataSeeder = new DataSeeder(this);
        if (!dataSeeder.isDataSeeded()) {
            dataSeeder.seedAllData();
        }

        // UPDATED: Initialize HTTP-based VoteManager instead of SharedPreferences version
        voteManagerHTTP = new VoteManagerHTTP(this);
        
        // Get current user info from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getString("student_number", "user123");
        currentUserName = sharedPreferences.getString("user_name", "Current User");

        // Initialize views
        initViews();
        
        // Setup toolbar with back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setupPost();
        setupCommentSection();
        loadComments();
    }

    private void initViews() {
        profileInitials = findViewById(R.id.profile_initials);
        userName = findViewById(R.id.user_name);
        postTime = findViewById(R.id.post_time);
        postHeading = findViewById(R.id.post_heading);
        postBody = findViewById(R.id.post_body);
        likesCount = findViewById(R.id.likes_count);
        commentsCount = findViewById(R.id.comments_count);
        likesContainer = findViewById(R.id.likes_container);
        commentsContainer = findViewById(R.id.comments_container);
        menuButton = findViewById(R.id.menu_button);

        // Get reference to the post like icon for visual feedback
        postLikeIcon = findViewById(R.id.post_like_icon);

        // Initialize comment section views
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentInput = findViewById(R.id.comment_input);
        postCommentButton = findViewById(R.id.post_comment_button);
        currentUserInitials = findViewById(R.id.current_user_initials);
        commentsEmptyState = findViewById(R.id.comments_empty_state);
    }

    private void setupPost() {
        // Get post data from Intent first (if available)
        Intent intent = getIntent();
        if (intent.hasExtra("POST_ID")) {
            // Use passed post data
            currentPostId = intent.getIntExtra("POST_ID", 1);
            String postTitle = intent.getStringExtra("POST_TITLE");
            String postContent = intent.getStringExtra("POST_CONTENT");
            String postAuthor = intent.getStringExtra("POST_AUTHOR");
            int postVotes = intent.getIntExtra("POST_VOTES", 0);
            
            currentPost = new ForumPost(
                    currentUserId,
                postAuthor,
                System.currentTimeMillis(),
                postTitle,
                postContent,
                0,
                postVotes,
                0,
                false
            );
        } else {
            // Fallback: Get post data from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String postsData = sharedPreferences.getString("posts_data", "[]");
            
            try {
                JSONArray postsArray = new JSONArray(postsData);
                
                // Get the first post for simplicity (student approach)
                if (postsArray.length() > 0) {
                    JSONObject postData = postsArray.getJSONObject(0);
                    
                    // Create post object
                    currentPostId = postData.getInt("POST_ID");
                    currentPost = new ForumPost(
                        postData.getString("STUDENT_NUMBER"),
                        postData.getString("AUTHOR_NAME"),
                        System.currentTimeMillis(),
                        postData.getString("TITLE"),
                        postData.getString("POST_QUESTION"),
                        0,
                        postData.getInt("VOTE_COUNT"),
                        0,
                        false
                    );
                } else {
                    // Use mock data if no posts found
                    currentPostId = 1;
                    currentPost = new ForumPost(
                        "123456",
                        "John Doe",
                        System.currentTimeMillis(),
                        "Sample Post Title",
                        "This is a sample post content for testing.",
                        0, 42, 5, false
                    );
                }
            } catch (Exception e) {
                // Simple fallback
                currentPostId = 1;
                currentPost = new ForumPost(
                    "123456",
                    "John Doe", 
                    System.currentTimeMillis(),
                    "Sample Post Title",
                    "This is a sample post content for testing.",
                    0, 42, 5, false
                );
            }
        }

        // Display post information
        profileInitials.setText(currentPost.getAuthorName().substring(0, 2).toUpperCase());
        userName.setText(currentPost.getAuthorName());
        postTime.setText(ForumUtils.formatTimeAgo(this, currentPost.getTimestamp()));
        postHeading.setText(currentPost.getTitle());
        postBody.setText(currentPost.getContent());
        likesCount.setText(String.valueOf(currentPost.getLikeCount()));
        commentsCount.setText(String.valueOf(currentPost.getCommentCount()));

        // UPDATED: Check vote status from server and update UI
        updatePostVoteUI();

        // Set up proper voting click listener
        likesContainer.setOnClickListener(v -> togglePostVote());

        commentsContainer.setOnClickListener(v -> {
            commentInput.requestFocus();
        });
    }

    /**
     * UPDATED: Get vote status from server and update UI
     */
    private void updatePostVoteUI() {
        if (postLikeIcon != null) {
            // Check vote status from PHP server
            voteManagerHTTP.checkPostVoteStatus(currentUserId, currentPostId, new VoteManagerHTTP.VoteStatusCallback() {
                @Override
                public void onVoteStatusResult(VoteManagerHTTP.VoteStatusResult result) {
                    if (result.isSuccess()) {
                        // Update UI on main thread
                        runOnUiThread(() -> {
                            if (result.hasVoted()) {
                                // User has voted - show as active/voted state
                                postLikeIcon.setColorFilter(ContextCompat.getColor(ForumPostActivity.this, R.color.upvote_active));
                                likesCount.setTextColor(ContextCompat.getColor(ForumPostActivity.this, R.color.upvote_active));
                            } else {
                                // User hasn't voted - show as inactive/default state
                                postLikeIcon.setColorFilter(ContextCompat.getColor(ForumPostActivity.this, R.color.upvote_inactive));
                                likesCount.setTextColor(ContextCompat.getColor(ForumPostActivity.this, R.color.upvote_inactive));
                            }
                            // Update vote count from server
                            currentPost.setLikeCount(result.getVoteCount());
                            likesCount.setText(String.valueOf(result.getVoteCount()));
                        });
                    } else {
                        // Fallback to inactive state if server check fails
                        runOnUiThread(() -> {
                            postLikeIcon.setColorFilter(ContextCompat.getColor(ForumPostActivity.this, R.color.upvote_inactive));
                            likesCount.setTextColor(ContextCompat.getColor(ForumPostActivity.this, R.color.upvote_inactive));
                        });
                    }
                }
            });
        }
    }

    /**
     * UPDATED: Toggle vote using HTTP requests to PHP server
     */
    private void togglePostVote() {
        // Show loading message to user
        Toast.makeText(this, "Processing vote...", Toast.LENGTH_SHORT).show();
        
        // Send vote request to PHP server
        voteManagerHTTP.togglePostVote(currentUserId, currentPostId, new VoteManagerHTTP.VoteCallback() {
            @Override
            public void onVoteResult(VoteManagerHTTP.VoteResult result) {
                // Update UI on main thread
                runOnUiThread(() -> {
                    if (result.isSuccess()) {
                        // Update UI with new vote count from server
                        currentPost.setLikeCount(result.getNewVoteCount());
                        likesCount.setText(String.valueOf(result.getNewVoteCount()));
                        
                        // Update visual feedback by checking current status
                        updatePostVoteUI();
                        
                        // Show success message to user
                        Toast.makeText(ForumPostActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // Show error message
                        Toast.makeText(ForumPostActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void setupCommentSection() {
        commentsList = new ArrayList<>();
        commentAdapter = new CommentAdapterHTTP(this, commentsList,
                new CommentAdapterHTTP.CommentInteractionListener() {
                    @Override
                    public void onCommentUpvoted(Comment comment, int position) {
                        // Handle comment upvote if needed
                    }

                    @Override
                    public void onReplyClicked(Comment comment, int position) {
                        commentInput.setText("@" + comment.getAuthorName() + " ");
                        commentInput.requestFocus();
                    }
                }, voteManagerHTTP, currentUserId);

        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Set current user initials
        currentUserInitials.setText(ForumUtils.getUserInitials(currentUserName));

        // Setup comment posting
        postCommentButton.setOnClickListener(v -> {
                String commentText = commentInput.getText().toString().trim();
            if (!TextUtils.isEmpty(commentText)) {
                if (isNetworkAvailable()) {
                    if (TextUtils.isEmpty(currentUserId) || currentPostId <= 0 || TextUtils.isEmpty(commentText)) {
                        Toast.makeText(this, "Missing required fields for comment", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    postNewComment(commentText);
                } else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadComments() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2688828/get_comments.php")
                .post(new FormBody.Builder()
                        .add("POST_ID", String.valueOf(currentPostId))
                        .build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("COMMENTS_RESPONSE", responseBody);
                runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getBoolean("success")) {
                            JSONArray comments = json.getJSONArray("comments");
                            List<Comment> commentList = new ArrayList<>();
                            for (int i = 0; i < comments.length(); i++) {
                                JSONObject comment = comments.getJSONObject(i);
                                commentList.add(new Comment(
                                        comment.getString("COMMENT_ID"),
                                        comment.getString("STUDENT_NUMBER"),
                                        comment.getString("AUTHOR_NAME"),
                                        parseDate(comment.getString("COMMENT_DATE")),
                                        comment.getString("STUDENT_COMMENT"),
                                        comment.getInt("VOTE_COUNT"),
                                        String.valueOf(currentPostId),
                                        comment.optString("PARENT_COMMENT_ID")
                                ));
                            }
                            commentAdapter.updateComments(commentList);
                    updateCommentCount();
                            commentsEmptyState.setVisibility(commentList.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ForumPostActivity.this, "Error parsing comments", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(ForumPostActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void postNewComment(String commentText) {
        // Validate input data
        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentPostId <= 0) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (commentText == null || commentText.trim().isEmpty()) {
            Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("COMMENT_POST", "Preparing to send comment with data:");
        Log.d("COMMENT_POST", "STUDENT_NUMBER: " + currentUserId);
        Log.d("COMMENT_POST", "POST_ID: " + currentPostId);
        Log.d("COMMENT_POST", "COMMENT_TEXT: " + commentText);

        postCommentButton.setEnabled(false);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        
        // Create the request body with proper encoding
        FormBody formBody = new FormBody.Builder()
                .add("STUDENT_NUMBER", currentUserId.trim())
                .add("POST_ID", String.valueOf(currentPostId))
                .add("COMMENT_TEXT", commentText.trim())
                .add("PARENT_COMMENT_ID", "") // Add empty parent comment ID
                .build();
                
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2688828/comment.php")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .post(formBody)
                .build();

        Log.d("COMMENT_POST", "Sending request to: " + request.url());
        Log.d("COMMENT_POST", "Request headers: " + request.headers());
        Log.d("COMMENT_POST", "Request body: " + formBody.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int responseCode = response.code();
                Log.d("COMMENT_POST_RESPONSE", "Response code: " + responseCode);
                
                String responseBody = response.body() != null ? response.body().string() : "";
                Log.d("COMMENT_POST_RESPONSE", "Raw response: " + responseBody);
                
                if (responseCode != 200) {
                    Log.e("COMMENT_POST_RESPONSE", "Server error response: " + responseBody);
                    runOnUiThread(() -> {
                        postCommentButton.setEnabled(true);
                        String errorMessage = "Server error (HTTP " + responseCode + ")";
                        try {
                            JSONObject errorJson = new JSONObject(responseBody);
                            if (errorJson.has("message")) {
                                errorMessage += ": " + errorJson.getString("message");
                            }
                        } catch (JSONException e) {
                            Log.e("COMMENT_POST_RESPONSE", "Error parsing error response", e);
                        }
                        Toast.makeText(ForumPostActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                
                if (responseBody.isEmpty()) {
                    Log.e("COMMENT_POST_RESPONSE", "Empty response received from server");
                    runOnUiThread(() -> {
                        postCommentButton.setEnabled(true);
                        Toast.makeText(ForumPostActivity.this, 
                            "Server returned empty response. Please try again.", 
                            Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                
                runOnUiThread(() -> {
                    postCommentButton.setEnabled(true);
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        if (json.getBoolean("success")) {
                            JSONObject commentData = json.getJSONObject("comment");
                            Comment newComment = new Comment(
                                    commentData.getString("COMMENT_ID"),
                                    commentData.getString("STUDENT_NUMBER"),
                                    commentData.getString("AUTHOR_NAME"),
                                    parseDate(commentData.getString("COMMENT_DATE")),
                                    commentData.getString("STUDENT_COMMENT"),
                                    commentData.getInt("VOTE_COUNT"),
                                    String.valueOf(currentPostId),
                                    commentData.optString("PARENT_COMMENT_ID")
                            );
                            commentAdapter.addComment(newComment);
                            updateCommentCount();
                            commentInput.setText("");
                            commentsEmptyState.setVisibility(View.GONE);
                            Toast.makeText(ForumPostActivity.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
                            
                            // Reload comments to ensure everything is in sync
                            loadComments();
                        } else {
                            String errorMessage = json.optString("message", "Unknown error occurred");
                            Log.e("COMMENT_POST_RESPONSE", "Server error: " + errorMessage);
                            Toast.makeText(ForumPostActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("COMMENT_POST_RESPONSE", "Error parsing response: " + responseBody, e);
                        Log.e("COMMENT_POST_RESPONSE", "Exception details: " + e.getMessage());
                        Toast.makeText(ForumPostActivity.this, 
                            "Error parsing response: " + e.getMessage(), 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("COMMENT_POST_RESPONSE", "Network error: " + e.getMessage());
                runOnUiThread(() -> {
                    postCommentButton.setEnabled(true);
                    Toast.makeText(ForumPostActivity.this, 
                        "Network error: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void updateCommentCount() {
        int count = commentAdapter.getItemCount();
        commentsCount.setText(String.valueOf(count));
        currentPost.setCommentCount(count);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }

    private long parseDate(String dateString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = format.parse(dateString);
            return date != null ? date.getTime() : System.currentTimeMillis();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }
}