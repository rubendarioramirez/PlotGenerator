package com.plotgen.rramirez.plotgenerator;

/**
 * Created by macintosh on 23/08/18.
 */

public class item_herojourney {

    int background;
    String herojourney_title, herojourney_act, herojourney_desc, herojourneyProject, herojourneyChar;

    public item_herojourney(int background, String herojourney_title, String herojourney_act, String herojourney_desc, String herojourneyProject, String herojourneyChar) {
        this.background = background;
        this.herojourney_title = herojourney_title;
        this.herojourney_act = herojourney_act;
        this.herojourney_desc = herojourney_desc;
        this.herojourneyChar = herojourneyChar;
        this.herojourneyProject = herojourneyProject;

    }

    public String getHerojourneyProject() {
        return herojourneyProject;
    }

    public void setHerojourneyProject(String herojourneyProject) {
        this.herojourneyProject = herojourneyProject;
    }

    public String getHerojourneyChar() {
        return herojourneyChar;
    }

    public void setHerojourneyChar(String herojourneyChar) {
        this.herojourneyChar = herojourneyChar;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getHerojourney_title() {
        return herojourney_title;
    }

    public void setHerojourney_title(String herojourney_title) {
        this.herojourney_title = herojourney_title;
    }

    public String getHerojourney_act() {
        return herojourney_act;
    }

    public void setHerojourney_act(String herojourney_act) {
        this.herojourney_act = herojourney_act;
    }

    public String getHerojourney_desc() {
        return herojourney_desc;
    }

    public void setHerojourney_desc(String herojourney_desc) {
        this.herojourney_desc = herojourney_desc;
    }
}
