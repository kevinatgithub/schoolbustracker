package app.kevin.dev.schoolservicetrackerparent.models;

import android.support.annotation.Nullable;

import java.util.ArrayList;

public class Parent {
    private double lat;
    private double lng;
    private ArrayList<String> vehicles;

    public Parent(double lat, double lng,@Nullable ArrayList<String> vehicles) {
        this.lat = lat;
        this.lng = lng;
        this.vehicles = vehicles;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<String> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<String> vehicles) {
        this.vehicles = vehicles;
    }
}
