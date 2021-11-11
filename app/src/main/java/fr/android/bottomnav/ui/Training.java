package fr.android.bottomnav.ui;

import java.util.ArrayList;

public class Training {
    private String date;
    private String distance;
    private String duration;
    private String address_start;
    private String address_end;
    private ArrayList<String> path;
    private ArrayList<String> rythm;

    public Training(String date, String distance, String duration, String address_start, String address_end, ArrayList<String> path, ArrayList<String> rythm){
        this.date = date;
        this.distance = distance;
        this.duration = duration;
        this.address_start = address_start;
        this.address_end = address_end;
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

    public String getAddress_start() {
        return address_start;
    }

    public String getAddress_end() {
        return address_end;
    }
}
