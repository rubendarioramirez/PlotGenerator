package com.plotgen.rramirez.plotgenerator.Model;

import android.net.Uri;

public class User {

    private String uid;
    private String name;
    private String email;
    private String uriString;
    private Uri picUrl;

    public User() {
    }

    public User(String uid, String name, String email, String uriString) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.uriString = uriString;
    }

    public User(String uid, String name, String email, Uri picUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
    }

    public User(String uid, String name, String email, String uriString, Uri picUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.uriString = uriString;
        this.picUrl = picUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public Uri getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(Uri picUrl) {
        this.picUrl = picUrl;
    }
}
