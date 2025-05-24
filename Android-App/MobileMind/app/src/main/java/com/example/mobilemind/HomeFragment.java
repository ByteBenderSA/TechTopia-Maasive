// MainActivity.java
package com.example.mobilemind;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private DiscussionAdapter discussionAdapter;
    private EditText searchEditText;
    private List<Discussion> discussionsList;
    private List<Discussion> filteredList;
    private TextView orderByText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views from the inflated layout
        setupViews(view);
        setupRecyclerView(view);
        setupSearch();
        
        // Load real posts data from SharedPreferences
        loadDiscussionsFromPreferences();
        
        return view;
    }

    private void setupViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        
        // Initialize lists
        discussionsList = new ArrayList<>();
        filteredList = new ArrayList<>();
    }

    private void setupRecyclerView(View view) {
        // Set up RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.discussionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Add divider between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Create and set adapter
        discussionAdapter = new DiscussionAdapter(filteredList);
        recyclerView.setAdapter(discussionAdapter);
    }

    private void setupSearch() {
        // Set up search functionality
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
    }

    private void loadDiscussionsFromPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String postsData = sharedPreferences.getString("posts_data", "[]");
        
        try {
            JSONArray postsArray = new JSONArray(postsData);
            discussionsList.clear();
            
            // Simple loop to load posts
            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject postData = postsArray.getJSONObject(i);
                
                // Create discussion object
                Discussion discussion = new Discussion(
                    postData.getInt("POST_ID"),
                    postData.getString("AUTHOR_NAME"),
                    postData.getString("TITLE"),
                    postData.getString("POST_QUESTION"),
                    "5m ago", // Simple timestamp
                    postData.getInt("VOTE_COUNT")
                );
                
                discussionsList.add(discussion);
            }
        } catch (Exception e) {
            // If something goes wrong, just load sample data
            loadSampleData();
        }
        
        // Update RecyclerView
        filteredList.addAll(discussionsList);
        if (discussionAdapter != null) {
            discussionAdapter.notifyDataSetChanged();
        }
    }
    
    private void loadSampleData() {
        // Simple fallback data
        discussionsList.add(new Discussion(
            1,
            "John Doe",
            "Help with Assignment 1",
            "I'm having trouble with the database connection...",
            "5m ago",
            12
        ));
        
        discussionsList.add(new Discussion(
            2,
            "Jane Smith", 
            "Study Group Formation",
            "Anyone interested in forming a study group for the upcoming exam?",
            "10m ago",
            8
        ));
        
        discussionsList.add(new Discussion(
            3,
            "Mike Johnson",
            "Android Studio Error",
            "Getting a compilation error when trying to run my app...",
            "15m ago",
            5
        ));
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

        if (discussionAdapter != null) {
            discussionAdapter.notifyDataSetChanged();
        }
    }
}