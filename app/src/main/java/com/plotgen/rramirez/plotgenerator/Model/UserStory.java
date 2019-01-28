package com.plotgen.rramirez.plotgenerator.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserStory {

    private String id;
    public int likeCount = 0;
    public Map<String, Boolean> likes = new HashMap<>();
    private String genre, project_id;
    private String project_name, plot, story;
    private long submitDate;
    private User user;

    public UserStory() {

    }

    public UserStory(String key, String project_name, String project_id, String genre, String plot, String story, Long tsLong, User user) {
        this.id = key;
        this.project_name = project_name;
        this.project_id = project_id;
        this.genre = genre;
        this.plot = plot;
        this.story = story;
        this.submitDate = tsLong;
        this.user = user;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
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

    public String getId() {
        return project_id;
    }

    public void setProjectId(String project_id) {
        this.project_id = project_id;
    }

    public String getProjectName() {
        return project_name;
    }

    public void setProjectName(String project_name) {
        this.project_name = project_name;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("genre", genre);
        result.put("project_id", project_id);
        result.put("project_name", project_name);
        result.put("plot", plot);
        result.put("story", story);
        result.put("id", id);
        result.put("user", user);
        result.put("date", submitDate);
        result.put("likeCount", likeCount);
        result.put("likes", likes);
        return result;
    }
}
