public class Discussion {
    private int id;
    private String title;
    private String lastPostDate;
    private boolean hasUnread;

    public Discussion(int id, String title, String lastPostDate, boolean hasUnread) {
        this.id = id;
        this.title = title;
        this.lastPostDate = lastPostDate;
        this.hasUnread = hasUnread;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLastPostDate() {
        return lastPostDate;
    }

    public boolean isHasUnread() {
        return hasUnread;
    }
}