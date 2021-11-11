package fr.android.bottomnav.ui;

import java.util.ArrayList;

public class Training {
    private String dateAndHour;
    private String distance;
    private String duration;
    private String address_start;
    private String address_end;
    private ArrayList<String> path;

    private boolean started;
    private boolean finished;

    public Training(){
        this.dateAndHour = "";
        this.distance = "";
        this.duration = "";
        this.address_start = "";
        this.address_end = "";
        this.path = new ArrayList<>();
        this.started = false;
        this.finished = false;
    }

    public Training(String dateAndHour, String distance, String duration, String address_start, String address_end, ArrayList<String> path){
        this.dateAndHour = dateAndHour;
        this.distance = distance;
        this.duration = duration;
        this.address_start = address_start;
        this.address_end = address_end;
        this.path = path;
        this.started = false;
        this.finished = false;
    }

    public String getDateAndHour() {
        return dateAndHour;
    }

    public void setDateAndHour(String dateAndHour) {
        this.dateAndHour = dateAndHour;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }

    public String getAddress_start() {
        return address_start;
    }

    public void setAddress_start(String address_start) {
        this.address_start = address_start;
    }

    public String getAddress_end() {
        return address_end;
    }

    public void setAddress_end(String address_end) {
        this.address_end = address_end;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
