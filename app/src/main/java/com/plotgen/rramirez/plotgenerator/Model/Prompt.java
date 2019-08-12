package com.plotgen.rramirez.plotgenerator.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Prompt {

    private String id;
    private long date;
    private User user;
    private String story;
    private Boolean selected;

    public Map<String, Boolean> likes = new HashMap<>();

    public Prompt() {
    }

    public Prompt(String id, String story, long date, User user, Boolean selected) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.story = story;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSubmitDate() {
        return date;
    }

    public void setSubmitDate(long submitDate) {
        this.date = submitDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
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
        result.put("date", date);
        result.put("story", story);
        result.put("selected", selected);

        return result;
    }
}
