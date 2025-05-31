// Discussion.java
package com.example.mobilemind;

public class Discussion {
    private int postId;
    private String author;
    private String title;
    private String content;
    private String time;
    private int votes;

    // Simple constructor that students would use
    public Discussion(int postId, String author, String title, String content, String time, int votes) {
        this.postId = postId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.time = time;
        this.votes = votes;
    }

    // Simple getters (what students typically write)
    public int getPostId() {
        return postId;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getVotes() {
        return votes;
    }

    // Keep old methods for compatibility
    public String getLastPostDate() {
        return time;
    }

    public boolean isHasUnread() {
        return false; // Simple approach
    }

    public int getId() {
        return postId; // Return actual post ID
    }
}