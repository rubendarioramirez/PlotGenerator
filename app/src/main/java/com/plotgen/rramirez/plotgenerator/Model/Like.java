package com.plotgen.rramirez.plotgenerator.Model;

public class Like {

    private String userId;
    private Boolean userLiked;

    public Like() {
    }

    public Like(String userId, Boolean userLiked) {
        this.userId = userId;
        this.userLiked = userLiked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(Boolean userLiked) {
        this.userLiked = userLiked;
    }
}
