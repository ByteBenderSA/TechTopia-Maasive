package com.example.mobilemind;

public class NotificationModel {
    private String title;
    private String time;

    public NotificationModel(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

}