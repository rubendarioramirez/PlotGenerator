package com.plotgen.rramirez.plotgenerator;

/**
 * Created by macintosh on 22/08/18.
 */

public class item {

    int background;
    String trigger_title;

    public item() {
    }

    public item(int background, String trigger_title) {
        this.background = background;
        this.trigger_title = trigger_title;
    }

    public int getBackground() {
        return background;
    }

    public String getTrigger_title() {
        return trigger_title;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTrigger_title(String trigger_title) {
        this.trigger_title = trigger_title;
    }
}

