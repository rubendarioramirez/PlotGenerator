package com.plotgen.rramirez.plotgenerator.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Genre {

    private String project_id;
    private String genre;
    private String uid;
    private String id;
    private String title;

    public Genre() {
    }



    public Genre(String uid, String key, String genre, String id, String title) {
        this.uid = uid;
        this.id = key;
        this.genre = genre;
        this.project_id = id;
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("id", id);
        result.put("project_id", project_id);
        result.put("title", title);
        return result;
    }
}
