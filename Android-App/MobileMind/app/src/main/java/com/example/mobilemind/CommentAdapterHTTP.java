package com.example.mobilemind;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * HTTP-based Comment Adapter for displaying comments with server-side voting
 */
public class CommentAdapterHTTP extends RecyclerView.Adapter<CommentAdapterHTTP.CommentViewHolder> {

    private List<Comment> commentList;
    private Context context;
    private CommentInteractionListener listener;
    private VoteManagerHTTP voteManagerHTTP;
    private String currentUserId;

    /**
     * Interface for handling comment interactions
     */
    public interface CommentInteractionListener {
        void onCommentUpvoted(Comment comment, int position);
        void onReplyClicked(Comment comment, int position);
    }

    public CommentAdapterHTTP(Context context, List<Comment> commentList, CommentInteractionListener listener, 
                             VoteManagerHTTP voteManagerHTTP, String currentUserId) {
        this.context = context;
        this.commentList = commentList;
        this.listener = listener;
        this.voteManagerHTTP = voteManagerHTTP;
        this.currentUserId = currentUserId;
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
        holder.commentAuthor.setText(comment.getAuthorName());
        holder.commentTimestamp.setText(ForumUtils.formatTimeAgo(context, comment.getTimestamp()).replace("Posted ", ""));
        holder.commentText.setText(comment.getContent());

        // Set upvote count
        holder.commentVoteCount.setText(String.valueOf(comment.getUpvoteCount()));

        // Check vote status from server and update UI
        checkAndUpdateCommentVoteUI(holder, comment);

        // Set click listeners
        holder.commentVoteButton.setOnClickListener(v -> {
            handleCommentVote(comment, position, holder);
        });

        holder.commentReplyButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReplyClicked(comment, position);
            }
        });

        if (holder.commentAuthor == null) {
            Log.e("CommentAdapterHTTP", "commentAuthor is null at position " + position);
        }
    }

    /**
     * Check vote status from server and update UI accordingly
     */
    private void checkAndUpdateCommentVoteUI(CommentViewHolder holder, Comment comment) {
        try {
            int commentId = Integer.parseInt(comment.getCommentId());
            
            voteManagerHTTP.checkCommentVoteStatus(currentUserId, commentId, 
                new VoteManagerHTTP.VoteStatusCallback() {
                    @Override
                    public void onVoteStatusResult(VoteManagerHTTP.VoteStatusResult result) {
                        if (result.isSuccess()) {
                            // Update UI on main thread
                            ((android.app.Activity) context).runOnUiThread(() -> {
                                updateVoteUI(holder, result.hasVoted());
                                // Update vote count from server
                                comment.setUpvoteCount(result.getVoteCount());
                                holder.commentVoteCount.setText(String.valueOf(result.getVoteCount()));
                            });
                        }
                    }
                });
        } catch (NumberFormatException e) {
            // Fallback for non-numeric comment IDs (mock data)
            updateVoteUI(holder, false);
        }
    }

    /**
     * Handle comment voting with HTTP requests
     */
    private void handleCommentVote(Comment comment, int position, CommentViewHolder holder) {
        try {
            int commentId = Integer.parseInt(comment.getCommentId());
            
            // Show loading state
            Toast.makeText(context, "Processing vote...", Toast.LENGTH_SHORT).show();
            
            voteManagerHTTP.toggleCommentVote(currentUserId, commentId, 
                new VoteManagerHTTP.VoteCallback() {
                    @Override
                    public void onVoteResult(VoteManagerHTTP.VoteResult result) {
                        // Update UI on main thread
                        ((android.app.Activity) context).runOnUiThread(() -> {
                            if (result.isSuccess()) {
                                // Update comment with new vote count
                                comment.setUpvoteCount(result.getNewVoteCount());
                                holder.commentVoteCount.setText(String.valueOf(result.getNewVoteCount()));
                                
                                // Update visual feedback based on new status
                                checkAndUpdateCommentVoteUI(holder, comment);
                                
                                // Notify listener
                                if (listener != null) {
                                    listener.onCommentUpvoted(comment, position);
                                }
                                
                                // Show success message
                                Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                // Show error message
                                Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
        } catch (NumberFormatException e) {
            // Fallback for non-numeric comment IDs (mock data)
            comment.setUpvoteCount(comment.getUpvoteCount() + 1);
            holder.commentVoteCount.setText(String.valueOf(comment.getUpvoteCount()));
            
            if (listener != null) {
                listener.onCommentUpvoted(comment, position);
            }
            
            Toast.makeText(context, "Vote added (mock mode)", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update the visual state of the vote button
     */
    private void updateVoteUI(CommentViewHolder holder, boolean hasVoted) {
        if (hasVoted) {
            // User has voted - show as active/voted state
            holder.commentVoteButton.setColorFilter(ContextCompat.getColor(context, R.color.upvote_active));
            holder.commentVoteCount.setTextColor(ContextCompat.getColor(context, R.color.upvote_active));
        } else {
            // User hasn't voted - show as inactive/default state
            holder.commentVoteButton.setColorFilter(ContextCompat.getColor(context, R.color.upvote_inactive));
            holder.commentVoteCount.setTextColor(ContextCompat.getColor(context, R.color.upvote_inactive));
        }
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
        TextView commentAuthor;
        TextView commentTimestamp;
        TextView commentText;
        TextView commentVoteCount;
        ImageButton commentVoteButton;
        ImageButton commentReplyButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.comment_author);
            commentTimestamp = itemView.findViewById(R.id.comment_timestamp);
            commentText = itemView.findViewById(R.id.comment_text);
            commentVoteCount = itemView.findViewById(R.id.comment_vote_count);
            commentVoteButton = itemView.findViewById(R.id.comment_vote_button);
            commentReplyButton = itemView.findViewById(R.id.comment_reply_button);
        }
    }
} 