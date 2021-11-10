package fr.android.bottomnav.ui;

import java.util.ArrayList;

public class Training {
    private String date;
    private String distance;
    private String duration;
    private String lat_start;
    private String lng_start;
    private String lat_end;
    private String lng_eng;
    private ArrayList<String> path;
    private ArrayList<String> rythm;

    public Training(String date, String distance, String duration, String lat_start, String lng_start, String lat_end, String lng_end, ArrayList<String> path, ArrayList<String> rythm){
        this.date = date;
        this.distance = distance;
        this.duration = duration;
        this.lat_start = lat_start;
        this.lng_start = lng_start;
        this.lat_end = lat_end;
        this.lng_eng = lng_end;
        this.path = path;
        this.rythm = rythm;
    }

    public String getDate() {
        return date;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public ArrayList<String> getRythm() {
        return rythm;
    }

    public String getLat_end() {
        return lat_end;
    }

    public String getLat_start() {
        return lat_start;
    }

    public String getLng_eng() {
        return lng_eng;
    }

    public String getLng_start() {
        return lng_start;
    }
}
