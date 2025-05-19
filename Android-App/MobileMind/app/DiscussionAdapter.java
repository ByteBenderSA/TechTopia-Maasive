// DiscussionAdapter.java
package com.example.mobilemind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {

    private List<Discussion> discussions;

    public DiscussionAdapter(List<Discussion> discussions) {
        this.discussions = discussions;
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_discussion, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        Discussion discussion = discussions.get(position);
        holder.titleTextView.setText(discussion.getTitle());
        holder.lastPostTextView.setText("Last post at " + discussion.getLastPostDate());

        if (discussion.isHasUnread()) {
            holder.unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.unreadIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView lastPostTextView;
        View unreadIndicator;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            lastPostTextView = itemView.findViewById(R.id.lastPostTextView);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
        }
    }
}