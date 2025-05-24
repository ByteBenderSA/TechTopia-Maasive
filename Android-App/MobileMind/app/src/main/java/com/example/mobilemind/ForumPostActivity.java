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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForumPostActivity extends AppCompatActivity implements CommentAdapter.CommentInteractionListener {

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
    private CommentAdapter commentAdapter;
    private List<Comment> commentsList;
    private EditText commentInput;
    private ImageButton postCommentButton;
    private TextView currentUserInitials;

    // Mock data for testing
    private ForumPost currentPost;
    private String currentUserId = "user123";
    private String currentUserName = "Current User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        initViews();
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

        // Initialize comment section views
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentInput = findViewById(R.id.comment_input);
        postCommentButton = findViewById(R.id.post_comment_button);
        currentUserInitials = findViewById(R.id.current_user_initials);
    }

    private void setupPost() {
        // Get post data from Intent first (if available)
        Intent intent = getIntent();
        if (intent.hasExtra("POST_ID")) {
            // Use passed post data
            String postTitle = intent.getStringExtra("POST_TITLE");
            String postContent = intent.getStringExtra("POST_CONTENT");
            String postAuthor = intent.getStringExtra("POST_AUTHOR");
            int postVotes = intent.getIntExtra("POST_VOTES", 0);
            
            currentPost = new ForumPost(
                "student123", // Simple fallback for student number
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
        postTime.setText("5m ago");
        postHeading.setText(currentPost.getTitle());
        postBody.setText(currentPost.getContent());
        likesCount.setText(String.valueOf(currentPost.getLikeCount()));
        commentsCount.setText(String.valueOf(currentPost.getCommentCount()));

        // Simple click listeners
        likesContainer.setOnClickListener(v -> {
            currentPost.setLikeCount(currentPost.getLikeCount() + 1);
            likesCount.setText(String.valueOf(currentPost.getLikeCount()));
        });

        commentsContainer.setOnClickListener(v -> {
            commentInput.requestFocus();
        });
    }

    private void setupCommentSection() {
        // Initialize comments list
        commentsList = new ArrayList<>();

        // Set up the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(layoutManager);

        // Set up the adapter
        commentAdapter = new CommentAdapter(this, commentsList, this);
        commentsRecyclerView.setAdapter(commentAdapter);

        // Set current user's initials
        currentUserInitials.setText(ForumUtils.getUserInitials(currentUserName));

        // Set up comment posting
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNewComment();
            }
        });
    }

    private void loadComments() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String commentsData = sharedPreferences.getString("comments_data", "[]");
            
            JSONArray commentsArray = new JSONArray(commentsData);
            commentsList.clear();
            
            // Get current post ID
            int currentPostId = getIntent().getIntExtra("POST_ID", 1);
            
            // Filter comments for current post
            for (int i = 0; i < commentsArray.length(); i++) {
                JSONObject commentData = commentsArray.getJSONObject(i);
                
                // Only load comments for current post
                if (commentData.getInt("POST_ID") == currentPostId) {
                    Comment comment = new Comment(
                        String.valueOf(commentData.getInt("COMMENT_ID")),
                        commentData.getString("STUDENT_NUMBER"),
                        commentData.getString("AUTHOR_NAME"),
                        parsePostTimestamp(commentData.getString("COMMENT_DATE")),
                        commentData.getString("STUDENT_COMMENT"),
                        commentData.getInt("VOTE_COUNT"),
                        String.valueOf(currentPostId),
                        null // Top-level comment for now
                    );
                    
                    commentsList.add(comment);
                }
            }
            
            // If no comments found, load mock comments as fallback
            if (commentsList.isEmpty()) {
                loadMockComments();
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading comments data", Toast.LENGTH_SHORT).show();
            loadMockComments();
        }
        
        commentAdapter.notifyDataSetChanged();
        updateCommentCount();
    }

    private void loadMockComments() {
        // Mock comments as fallback
        commentsList.clear();

        commentsList.add(new Comment(
                "comment1",
                "user456",
                "Anna Smith",
                System.currentTimeMillis() - 180000, // 3 minutes ago
                "Thanks for sharing these resources! The video on balanced trees was particularly helpful for me.",
                12,
                "post123",
                null));

        commentsList.add(new Comment(
                "comment2",
                "user789",
                "Mike Johnson",
                System.currentTimeMillis() - 300000, // 5 minutes ago
                "Could you explain more about the time complexity of tree traversals? I'm still confused about the recursive approach.",
                5,
                "post123",
                null));

        commentsList.add(new Comment(
                "comment3",
                "user234",
                "Sarah Williams",
                System.currentTimeMillis() - 600000, // 10 minutes ago
                "I found a great YouTube channel that explains these concepts really well. I'll share the link in our study group!",
                8,
                "post123",
                null));
    }

    private void postNewComment() {
        String commentText = commentInput.getText().toString().trim();

        if (TextUtils.isEmpty(commentText)) {
            return;
        }

        // Get the actual POST_ID from intent
        int currentPostId = getIntent().getIntExtra("POST_ID", 1);

        // Create a new comment
        Comment newComment = new Comment(
                "comment" + (commentsList.size() + 1), // In real app, generate a unique ID
                currentUserId,
                currentUserName,
                System.currentTimeMillis(),
                commentText,
                0,
                String.valueOf(currentPostId), // Use actual post ID
                null // Top-level comment
        );

        // Add the comment to the list
        commentsList.add(0, newComment); // Add to the top
        commentAdapter.notifyItemInserted(0);

        // Scroll to the new comment
        commentsRecyclerView.scrollToPosition(0);

        // Clear the input field
        commentInput.setText("");

        // Update comment count
        updateCommentCount();

        // In a real app, you would save the comment to your database here
    }

    private void updateCommentCount() {
        // Update the post's comment count
        currentPost.setCommentCount(commentsList.size());
        commentsCount.setText(String.valueOf(currentPost.getCommentCount()));
    }

    private void toggleLike() {
        // Toggle like status and update UI
        // In a real app, you would save this to your database
        currentPost.setLikeCount(currentPost.getLikeCount() + 1);
        likesCount.setText(String.valueOf(currentPost.getLikeCount()));
    }

    private void showPostOptions() {
        // Show a menu with options for the post
        Toast.makeText(this, "Post options", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentUpvoted(Comment comment, int position) {
        // Increment upvote count
        comment.setUpvoteCount(comment.getUpvoteCount() + 1);
        commentAdapter.updateComment(position, comment);

        // In a real app, you would save this to your database
    }

    @Override
    public void onReplyClicked(Comment comment, int position) {
        // Focus on comment input and set hint for replying
        commentInput.requestFocus();
        commentInput.setHint("Reply to " + comment.getAuthorName() + "...");

        // In a full implementation, you would save the parentCommentId
        // when posting the reply
    }

    /**
     * Parse timestamp from database date string
     */
    private long parsePostTimestamp(String dateString) {
        // Simple approach for student project
        try {
            // Handle MySQL DATETIME format: "2024-01-15 14:30:25"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = dateFormat.parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            // If parsing fails, try simpler approach
            try {
                // Maybe it's just a date: "2024-01-15"
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = simpleDateFormat.parse(dateString);
                if (date != null) {
                    return date.getTime();
                }
            } catch (ParseException e2) {
                // Still failed, continue to fallback
            }
        } catch (Exception e) {
            // Any other error, continue to fallback
        }
        
        // Fallback: return a recent timestamp (student-friendly approach)
        long currentTime = System.currentTimeMillis();
        // Return a time between 1-60 minutes ago
        long minutesAgo = (long) (Math.random() * 60 + 1);
        return currentTime - (minutesAgo * 60 * 1000);
    }
}