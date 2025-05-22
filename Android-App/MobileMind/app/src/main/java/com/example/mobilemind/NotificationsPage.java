package com.example.mobilemind;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationsPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_notifications);  // Link to your screen layout

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fake data for now
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationModel("Meeting at 3PM", "10 mins ago"));
        notificationList.add(new NotificationModel("New artwork uploaded", "1 hour ago"));
        notificationList.add(new NotificationModel("System update available", "Yesterday"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
    }
}
