package com.example.mobilemind;

/**
 * Model class for a forum post
 */
public class ForumPost {
    private String authorId;
    private String authorName;
    private long timestamp;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int commentCount;
    private boolean isSaved;

    // Default constructor
    public ForumPost() {
    }

    public ForumPost(String authorId, String authorName, long timestamp, String title,
                     String content, int viewCount, int likeCount, int commentCount, boolean isSaved) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.timestamp = timestamp;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.isSaved = isSaved;
    }

    // Getters and Setters
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}