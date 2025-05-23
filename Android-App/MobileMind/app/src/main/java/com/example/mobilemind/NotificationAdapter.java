package com.example.mobilemind;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        ImageView icon;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.notification_title);
            time = view.findViewById(R.id.notification_timestamp);
            icon = view.findViewById(R.id.notification_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationModel model = notificationList.get(position);
        holder.title.setText(model.getTitle());
        holder.time.setText(model.getTime());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

