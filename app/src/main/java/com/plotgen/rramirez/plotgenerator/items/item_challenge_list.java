package com.plotgen.rramirez.plotgenerator.items;

/**
 * Created by macintosh on 22/08/18.
 */

public class item_challenge_list {

    String challenge_id;
    String challenge_title;
    String challenge_description;
    String challenge_recommendedAct;
    
    public item_challenge_list() {
    }



    public item_challenge_list(String challenge_id, String challenge_title, String challenge_recommendedAct, String challenge_description) {
        this.challenge_id = challenge_id;
        this.challenge_title = challenge_title;
        this.challenge_recommendedAct = challenge_recommendedAct;
        this.challenge_description = challenge_description;
    }


    public String getChallenge_title() {
        return challenge_title;
    }

    public String getChallenge_description() {
        return challenge_description;
    }

    public void setChallenge_title(String challenge_title) {
        this.challenge_title = challenge_title;
    }

    public void setChallenge_description(String challenge_description) {
        this.challenge_description = challenge_description;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }


    public String getChallenge_recommendedAct() {
        return challenge_recommendedAct;
    }

    public void setChallenge_recommendedAct(String challenge_recommendedAct) {
        this.challenge_recommendedAct = challenge_recommendedAct;
    }
}

