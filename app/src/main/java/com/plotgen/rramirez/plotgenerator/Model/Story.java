package com.plotgen.rramirez.plotgenerator.Model;

public class Story {

    private String title;
    private String genre;
    private String chalenge;
    private long submitDate;
    private User user;

    public Story() {
    }

    public Story(String title, String genre, String chalenge, User user) {
        this.title = title;
        this.genre = genre;
        this.chalenge = chalenge;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getChalenge() {
        return chalenge;
    }

    public void setChalenge(String chalenge) {
        this.chalenge = chalenge;
    }

    public long getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(long submitDate) {
        this.submitDate = submitDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
