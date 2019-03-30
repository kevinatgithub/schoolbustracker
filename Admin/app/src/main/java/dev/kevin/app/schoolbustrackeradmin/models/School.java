package dev.kevin.app.schoolbustrackeradmin.models;

public class School {

    private int id;
    private String name,lat,lng;

    public School(int id, String name, String lat, String lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
