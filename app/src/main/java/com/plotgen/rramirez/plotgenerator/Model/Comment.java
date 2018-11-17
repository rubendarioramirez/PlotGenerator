package com.plotgen.rramirez.plotgenerator.Model;

public class Comment {
    private String userComment;
    private User user;

    public Comment() {
    }

    public Comment(String userComment, User user) {
        this.userComment = userComment;
        this.user = user;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

