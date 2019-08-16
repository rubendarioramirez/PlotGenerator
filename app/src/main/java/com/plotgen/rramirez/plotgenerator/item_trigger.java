package com.plotgen.rramirez.plotgenerator;

/**
 * Created by macintosh on 22/08/18.
 */

public class item_trigger {

    int background;
    String trigger_title;
    String trigger_author;

    public item_trigger() {
    }

    public item_trigger(int background, String trigger_title, String trigger_author) {
        this.background = background;
        this.trigger_title = trigger_title;
        this.trigger_author = trigger_author;
    }

    public int getBackground() {
        return background;
    }

    public String getTrigger_title() {
        return trigger_title;
    }

    public String getTrigger_author() {
        return trigger_author;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTrigger_title(String trigger_title) {
        this.trigger_title = trigger_title;
    }

    public void setTrigger_author(String trigger_author) {
        this.trigger_author = trigger_author;
    }
}

