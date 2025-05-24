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
            notificationList.add(new NotificationModel("New comment on your post", "10 mins ago"));
            notificationList.add(new NotificationModel("Assignment deadline reminder", "1 hour ago"));
            notificationList.add(new NotificationModel("Study group invitation", "3 hours ago"));
            notificationList.add(new NotificationModel("New post in Android Development", "6 hours ago"));
            notificationList.add(new NotificationModel("System maintenance completed", "Yesterday"));

            adapter = new NotificationAdapter(notificationList);
            recyclerView.setAdapter(adapter);
        }
        
        return view;
    }
}
