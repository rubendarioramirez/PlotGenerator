package com.plotgen.rramirez.plotgenerator.items;

/**
 * Created by macintosh on 22/08/18.
 */

public class item_outline {

    String outline_id;
    String outline_title;
    String outline_description;
    String outline_characters;

    public item_outline() {
    }

    public item_outline(String outline_id, String outline_title, String outline_description, String outline_characters) {
        this.outline_id = outline_id;
        this.outline_title = outline_title;
        this.outline_description = outline_description;
        this.outline_characters = outline_characters;
    }


    public String getOutline_title() {
        return outline_title;
    }

    public String getOutline_description() {
        return outline_description;
    }

    public void setOutline_title(String trigger_title) {
        this.outline_title = trigger_title;
    }

    public void setOutline_description(String outline_description) {
        this.outline_description = outline_description;
    }

    public String getOutline_characters() {
        return outline_characters;
    }

    public void setOutline_characters(String outline_characters) {
        this.outline_characters = outline_characters;
    }

    public String getOutline_id() {
        return outline_id;
    }

    public void setOutline_id(String outline_id) {
        this.outline_id = outline_id;
    }
}

