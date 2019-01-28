package com.plotgen.rramirez.plotgenerator.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Genre {

    private String project_id, genre;
    private String uid;
    private String id;

    public Genre() {
    }

    public Genre(String uid, String key, String genre, String id) {
        this.uid = uid;
        this.id = key;
        this.genre = genre;
        this.project_id = id;
    }

    public String getGenre() {
        return uid;
    }

    public void setGenre(String genre) {
        this.uid = genre;
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("id", id);
        result.put("project_id", project_id);
        return result;
    }
}
