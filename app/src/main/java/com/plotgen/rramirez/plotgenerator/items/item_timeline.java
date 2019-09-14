package com.plotgen.rramirez.plotgenerator.items;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by macintosh on 22/08/18.
 */

public class item_timeline {

    String timeline_id;
    String timeline_title;
    String timeline_description;
    String timeline_date;

    public item_timeline() {
    }


    public item_timeline(String timeline_id, String timeline_title, String timeline_description, String timeline_date) {
        this.timeline_id = timeline_id;
        this.timeline_title = timeline_title;
        this.timeline_description = timeline_description;
        this.timeline_date = timeline_date;
    }


    public String getTimeline_title() {
        return timeline_title;
    }

    public String getTimeline_description() {
        return timeline_description;
    }

    public void setTimeline_title(String timeline_title) {
        this.timeline_title = timeline_title;
    }

    public void setTimeline_description(String timeline_description) {
        this.timeline_description = timeline_description;
    }

    public String getTimeline_id() {
        return timeline_id;
    }

    public void setTimeline_id(String timeline_id) {
        this.timeline_id = timeline_id;
    }

    public String getTimeline_date() {
        return timeline_date;
    }

    public void setTimeline_date(String timeline_date) {
        this.timeline_date = timeline_date;
    }
}

