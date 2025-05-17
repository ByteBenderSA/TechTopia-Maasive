package com.example.mobilemind;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        initViews();
        setupPost();
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
    }

    private void setupPost() {
        // Set user information
        profileInitials.setText("JD"); // Get from user's name
        userName.setText("John Doe");
        postTime.setText("Posted 5m ago");

        // Set post content
        postHeading.setText("Learning Resources for Module 3: Data Structures");
        postBody.setText("Hello everyone! I've compiled some helpful resources for " +
                "this week's module on data structures. These articles and videos have been " +
                "extremely useful for me, and I thought they might help others who are " +
                "struggling with linked lists and trees. Let me know if you have any other recommendations!");

        // Set interaction stats
        likesCount.setText("42");
        commentsCount.setText("7");

        // Set up button click listeners
        likesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle like button click
            }
        });

        commentsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle comments button click
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show options menu
            }
        });
    }

    /**
     * Helper method to get user's initials from their full name
     * @param name User's full name
     * @return Initials (up to 2 characters)
     */
    private String getUserInitials(String name) {
        StringBuilder initials = new StringBuilder();
        String[] nameParts = name.split(" ");

        for (int i = 0; i < Math.min(2, nameParts.length); i++) {
            if (!nameParts[i].isEmpty()) {
                initials.append(Character.toUpperCase(nameParts[i].charAt(0)));
            }
        }

        return initials.toString();
    }
}