package com.plotgen.rramirez.plotgenerator.Model;

public class Challenge {
    private String id;
    private String name;
    private Story story;

    public Challenge() {
    }

    public Challenge(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
