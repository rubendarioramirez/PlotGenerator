package com.plotgen.rramirez.plotgenerator.Model;

public class Comment {
    private String userId;
    private String userName;
    private String userPic;
    private String userComment;

    public Comment() {
    }

    public Comment(String userId, String userName, String userPic, String userComment) {
        this.userId = userId;
        this.userName = userName;
        this.userPic = userPic;
        this.userComment = userComment;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}

