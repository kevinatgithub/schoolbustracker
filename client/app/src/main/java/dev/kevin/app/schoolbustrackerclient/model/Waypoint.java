package dev.kevin.app.schoolbustrackerclient.model;

public class Waypoint {
    private double distance;
    private double[] location;
    private String name;

    public Waypoint(double distance, double[] location, String name) {
        this.distance = distance;
        this.location = location;
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public double[] getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
