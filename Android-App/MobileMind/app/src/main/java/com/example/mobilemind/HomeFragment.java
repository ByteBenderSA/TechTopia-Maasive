// MainActivity.java
package com.example.mobilemind;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends AppCompatActivity {

    private DiscussionAdapter discussionAdapter;
    private EditText searchEditText;
    private List<Discussion> discussionsList;
    private List<Discussion> filteredList;
    private TextView orderByText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // Initialize lists
        ArrayList<>discussionsList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.discussionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Create and set adapter
        discussionAdapter = new DiscussionAdapter(filteredList);
        recyclerView.setAdapter(discussionAdapter);

        // Set up search functionality
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDiscussions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });

        // Load sample data
        loadSampleData();
    }

    private void loadSampleData() {
        discussionsList.add(new Discussion(1, "Allocated Mentor", "8 Apr, 20:49", false));
        discussionsList.add(new Discussion(2, "Physics tutorials during protest", "2 Mar, 12:28", false));
        discussionsList.add(new Discussion(3, "WSOE", "15 Feb, 21:18", false));
        discussionsList.add(new Discussion(4, "Orientation", "7 Feb, 08:24", true));
        discussionsList.add(new Discussion(5, "Times for Orientation", "5 Feb, 23:31", true));
        discussionsList.add(new Discussion(6, "Course Materials", "2 Feb, 14:15", false));
        discussionsList.add(new Discussion(7, "Study Group Formation", "1 Feb, 09:30", false));

        // Add all to filtered list initially
        filteredList.addAll(discussionsList);
        discussionAdapter.notifyDataSetChanged();
    }

    private void filterDiscussions(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(discussionsList);
        } else {
            String lowercaseQuery = query.toLowerCase();
            for (Discussion discussion : discussionsList) {
                if (discussion.getTitle().toLowerCase().contains(lowercaseQuery)) {
                    filteredList.add(discussion);
                }
            }
        }

        discussionAdapter.notifyDataSetChanged();
    }
}