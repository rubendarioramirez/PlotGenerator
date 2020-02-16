package com.plotgen.rramirez.plotgenerator.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PublishedStory {

    private String id;
    private String title;
    private String genre;
    private long date;
    private User user;
    private String story;


    public PublishedStory() {
    }

    public PublishedStory(String id, String title, String story, String genre, long date, User user) {
        this.id = id;
        this.title = title;
        this.genre = genre;
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
        result.put("date", date);
        return result;
    }
}
