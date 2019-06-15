package com.plotgen.rramirez.plotgenerator.Model;

public class Character {
    private String id;
    private String name;
    private String project_name;
    private String gender;
    private String role;
    private int challengesDone;

    public Character(String id, String name, String project_name, String role, String gender, int challengesDone) {
        this.id = id;
        this.name = name;
        this.project_name = project_name;
        this.gender = gender;
        this.role = role;
        this.challengesDone = challengesDone;
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

    public String getProject_name() {
        return project_name;
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getChallengesDone() {
        return challengesDone;
    }

    public void setChallengesDone(int challengesDone) {
        this.challengesDone = challengesDone;
    }

}


