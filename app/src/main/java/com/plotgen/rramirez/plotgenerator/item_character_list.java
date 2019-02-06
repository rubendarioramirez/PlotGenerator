package com.plotgen.rramirez.plotgenerator;

/**
 * Created by macintosh on 22/08/18.
 */

public class item_character_list {

    String image;
    String name;
    String role;
    String gender;
    String completion;


    public item_character_list(String image, String name, String role, String gender, String completion) {
        this.image = image;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.completion = completion;
    }

    public String getImage() { return image; }
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getCompletion() {
        return completion;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) { this.role = role;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

