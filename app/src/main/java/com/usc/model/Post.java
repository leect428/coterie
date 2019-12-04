package com.usc.model;

import java.util.Date;

public class Post {
    private String text;
    private Long time;
    private User user;
    public Post() {}
    public Post(String text, Long time, User user) {
        this.text = text;
        this.time = time;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public Long getTime() {
        return time;
    }

    public User getUser() {
        return user;
    }
}
