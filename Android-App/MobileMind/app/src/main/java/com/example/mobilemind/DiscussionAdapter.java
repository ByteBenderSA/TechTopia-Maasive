// DiscussionAdapter.java
package com.example.mobilemind;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {

    private List<Discussion> discussions;
    private Context context;

    public DiscussionAdapter(List<Discussion> discussions) {
        this.discussions = discussions;
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_discussion, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        Discussion discussion = discussions.get(position);
        
        // Simple display (student approach)
        holder.titleTextView.setText(discussion.getTitle());
        holder.lastPostTextView.setText("By " + discussion.getAuthor() + " • " + discussion.getTime());
        
        // Set profile initials
        String initials = ForumUtils.getUserInitials(discussion.getAuthor());
        holder.profileInitials.setText(initials);
        
        // Set different colored backgrounds for variety
        int[] backgroundColors = {
            R.drawable.circle_background,
            R.drawable.circle_background_blue,
            R.drawable.circle_background_orange,
            R.drawable.circle_background_purple
        };
        int colorIndex = position % backgroundColors.length;
        holder.profileInitials.setBackgroundResource(backgroundColors[colorIndex]);

        // Simple click listener 
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForumPostActivity.class);
            // Pass post data to ForumPostActivity
            intent.putExtra("POST_ID", discussion.getPostId());
            intent.putExtra("POST_TITLE", discussion.getTitle());
            intent.putExtra("POST_CONTENT", discussion.getContent());
            intent.putExtra("POST_AUTHOR", discussion.getAuthor());
            intent.putExtra("POST_VOTES", discussion.getVotes());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView lastPostTextView;
        TextView profileInitials;
        View unreadIndicator;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            lastPostTextView = itemView.findViewById(R.id.lastPostTextView);
            profileInitials = itemView.findViewById(R.id.profileInitials);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);
        }
    }
}