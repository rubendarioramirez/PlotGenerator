package com.plotgen.rramirez.plotgenerator.Model;

public class Like {

    private int likeCount;
    private User user;

    public Like() {
    }

    public Like(int likeCount, User user) {
        this.likeCount = likeCount;
        this.user = user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
