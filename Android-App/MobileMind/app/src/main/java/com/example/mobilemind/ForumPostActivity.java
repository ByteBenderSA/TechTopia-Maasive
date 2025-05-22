package com.example.mobilemind;

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

import java.util.ArrayList;
import java.util.List;

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
        // Create a mock post
        currentPost = new ForumPost(
                "author123",
                "John Doe",
                System.currentTimeMillis() - 300000, // 5 minutes ago
                "Learning Resources for Module 3: Data Structures",
                "Hello everyone! I've compiled some helpful resources for " +
                        "this week's module on data structures. These articles and videos have been " +
                        "extremely useful for me, and I thought they might help others who are " +
                        "struggling with linked lists and trees. Let me know if you have any other recommendations!",
                152,
                42,
                7,
                false);

        // Set user information
        profileInitials.setText(ForumUtils.getUserInitials(currentPost.getAuthorName()));
        userName.setText(currentPost.getAuthorName());
        postTime.setText(ForumUtils.formatTimeAgo(this, currentPost.getTimestamp()));

        // Set post content
        postHeading.setText(currentPost.getTitle());
        postBody.setText(currentPost.getContent());

        // Set interaction stats
        likesCount.setText(String.valueOf(currentPost.getLikeCount()));
        commentsCount.setText(String.valueOf(currentPost.getCommentCount()));

        // Set up button click listeners
        likesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle like button click
                toggleLike();
            }
        });

        commentsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Focus on comment input
                commentInput.requestFocus();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show options menu
                showPostOptions();
            }
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
        // In a real app, you would load comments from your database
        // For now, let's add some mock comments

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

        commentAdapter.notifyDataSetChanged();
        updateCommentCount();
    }

    private void postNewComment() {
        String commentText = commentInput.getText().toString().trim();

        if (TextUtils.isEmpty(commentText)) {
            return;
        }

        // Create a new comment
        Comment newComment = new Comment(
                "comment" + (commentsList.size() + 1), // In real app, generate a unique ID
                currentUserId,
                currentUserName,
                System.currentTimeMillis(),
                commentText,
                0,
                "post123", // In real app, use actual post ID
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
}