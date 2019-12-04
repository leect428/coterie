package com.usc.model;

import java.util.ArrayList;

public class Group {
    private ArrayList<Post> posts;
    private ArrayList<User> members;
    private String name;
    private String description;

    public Group() {
        members = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setDescription(String d) {
        this.description = d;
    }

    public void addMember(User u) {
        members.add(u);
    }
}
