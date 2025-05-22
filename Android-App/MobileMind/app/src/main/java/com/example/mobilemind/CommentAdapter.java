package com.example.mobilemind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for displaying comments in a RecyclerView
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private Context context;
    private CommentInteractionListener listener;

    /**
     * Interface for handling comment interactions
     */
    public interface CommentInteractionListener {
        void onCommentUpvoted(Comment comment, int position);
        void onReplyClicked(Comment comment, int position);
    }

    public CommentAdapter(Context context, List<Comment> commentList, CommentInteractionListener listener) {
        this.context = context;
        this.commentList = commentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        // Set commenter information
        holder.commenterInitials.setText(ForumUtils.getUserInitials(comment.getAuthorName()));
        holder.commenterName.setText(comment.getAuthorName());

        // Format time ago
        String timeAgo = ForumUtils.formatTimeAgo(context, comment.getTimestamp()).replace("Posted ", "");
        holder.commentTime.setText(timeAgo);

        // Set comment content
        holder.commentBody.setText(comment.getContent());

        // Set upvote count
        holder.commentUpvotesCount.setText(String.valueOf(comment.getUpvoteCount()));

        // Set click listeners
        holder.commentUpvoteContainer.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCommentUpvoted(comment, position);
            }
        });

        holder.replyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReplyClicked(comment, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    /**
     * Update the comments list and refresh the view
     * @param newComments new list of comments
     */
    public void updateComments(List<Comment> newComments) {
        this.commentList = newComments;
        notifyDataSetChanged();
    }

    /**
     * Add a new comment to the list
     * @param comment the comment to add
     */
    public void addComment(Comment comment) {
        commentList.add(comment);
        notifyItemInserted(commentList.size() - 1);
    }

    /**
     * Update a specific comment (e.g., after upvoting)
     * @param position position of the comment to update
     * @param comment updated comment object
     */
    public void updateComment(int position, Comment comment) {
        if (position >= 0 && position < commentList.size()) {
            commentList.set(position, comment);
            notifyItemChanged(position);
        }
    }

    /**
     * ViewHolder class for comment items
     */
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commenterInitials;
        TextView commenterName;
        TextView commentTime;
        TextView commentBody;
        TextView commentUpvotesCount;
        TextView replyButton;
        View commentUpvoteContainer;
        CardView commenterImageContainer;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commenterInitials = itemView.findViewById(R.id.commenter_initials);
            commenterName = itemView.findViewById(R.id.commenter_name);
            commentTime = itemView.findViewById(R.id.comment_time);
            commentBody = itemView.findViewById(R.id.comment_body);
            commentUpvotesCount = itemView.findViewById(R.id.comment_upvotes_count);
            replyButton = itemView.findViewById(R.id.reply_button);
            commentUpvoteContainer = itemView.findViewById(R.id.comment_upvote_container);
            commenterImageContainer = itemView.findViewById(R.id.commenter_image_container);
        }
    }
}