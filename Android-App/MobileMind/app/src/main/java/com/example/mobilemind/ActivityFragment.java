package com.example.mobilemind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        
        // Check if context is available
        if (getContext() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Simple data setup (typical student approach)
            notificationList = new ArrayList<>();
            // Recent notifications (last 24 hours)
            notificationList.add(new NotificationModel("John Smith replied to your question about RecyclerView implementation", "5 mins ago"));
            notificationList.add(new NotificationModel("Your question 'How to implement Firebase Authentication?' received 3 new upvotes", "15 mins ago"));
            notificationList.add(new NotificationModel("Sarah Johnson commented on your post about Android Studio setup", "1 hour ago"));
            notificationList.add(new NotificationModel("New answer to your question 'Best practices for Android UI design'", "2 hours ago"));
            notificationList.add(new NotificationModel("Your comment on 'Understanding Android Lifecycle' received 2 upvotes", "3 hours ago"));
            
            // Older notifications (24-48 hours)
            notificationList.add(new NotificationModel("Michael Brown started following your question about Kotlin Coroutines", "Yesterday"));
            notificationList.add(new NotificationModel("Your question 'Android Navigation Component Tutorial' was marked as helpful", "Yesterday"));
            notificationList.add(new NotificationModel("New comment on your post about Android Architecture Components", "Yesterday"));
            
            // System notifications
            notificationList.add(new NotificationModel("Welcome to MobileMind! Start by asking your first question", "2 days ago"));
            notificationList.add(new NotificationModel("Complete your profile to get more engagement", "3 days ago"));

            adapter = new NotificationAdapter(notificationList);
            recyclerView.setAdapter(adapter);
        }
        
        return view;
    }
}
