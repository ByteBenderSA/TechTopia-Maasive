package com.example.mobilemind;

/**
 * Model class for a comment on a forum post
 */
public class Comment {
    private String commentId;
    private String authorId;
    private String authorName;
    private long timestamp;
    private String content;
    private int upvoteCount;
    private String parentPostId;
    private String parentCommentId; // For replies to comments, null if it's a top-level comment

    // Default constructor
    public Comment() {
    }

    public Comment(String commentId, String authorId, String authorName, long timestamp,
                   String content, int upvoteCount, String parentPostId, String parentCommentId) {
        this.commentId = commentId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.content = content;
        this.upvoteCount = upvoteCount;
        this.parentPostId = parentPostId;
        this.parentCommentId = parentCommentId;
    }

    // Getters and Setters
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(int upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    // Compatibility methods for vote count (alias for upvoteCount)
    public int getVoteCount() {
        return upvoteCount;
    }

    public void setVoteCount(int voteCount) {
        this.upvoteCount = voteCount;
    }

    public String getParentPostId() {
        return parentPostId;
    }

    public void setParentPostId(String parentPostId) {
        this.parentPostId = parentPostId;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    /**
     * Checks if this comment is a reply to another comment
     * @return true if this is a reply to another comment, false if it's a top-level comment
     */
    public boolean isReply() {
        return parentCommentId != null && !parentCommentId.isEmpty();
    }
}