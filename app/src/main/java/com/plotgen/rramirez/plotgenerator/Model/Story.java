package com.plotgen.rramirez.plotgenerator.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class Story {

    private String id;
    private String title;
    private String genre;
    private String chalenge;
    private long date;
    private User user;
    private String story;

    public int likeCount = 0;
    public Map<String, Boolean> likes = new HashMap<>();

    public Story() {
    }

    public Story(String id, String title,String story, String genre, String chalenge, long date, User user) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.chalenge = chalenge;
        this.date = date;
        this.user = user;
        this.story = story;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("user", user);
        result.put("title", title);
        result.put("genre", genre);
        result.put("chalenge", chalenge);
        result.put("date", date);
        result.put("likeCount", likeCount);
        result.put("likes", likes);

        return result;
    }
}
