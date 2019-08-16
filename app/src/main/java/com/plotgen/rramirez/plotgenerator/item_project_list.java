package com.plotgen.rramirez.plotgenerator;

/**
 * Created by macintosh on 22/08/18.
 */

public class item_project_list {

    String id;
    String name;
    String genre;
    String characters;


    public item_project_list(String id, String name, String genre, String characters) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.characters = characters;
    }

    public String getName() {
        return name;
    }

    public String getgenre() {
        return genre;
    }

    public String getcharacters() {
        return characters;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setgenre(String genre) { this.genre = genre;
    }

    public void setcharacters(String characters) {
        this.characters = characters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

