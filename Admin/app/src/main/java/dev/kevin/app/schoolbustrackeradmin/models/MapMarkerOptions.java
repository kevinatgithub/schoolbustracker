package dev.kevin.app.schoolbustrackeradmin.models;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class MapMarkerOptions{
    private Vehicle vehicle;
    private MarkerOptions markerOptions;

    public MapMarkerOptions(Vehicle vehicle) {
        this.vehicle = vehicle;
        markerOptions = new MarkerOptions()
                .position(new LatLng(vehicle.getLat(),vehicle.getLng()))
                .setTitle(vehicle.getPlate_no() + " - " + vehicle.getDriver());
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        markerOptions.setPosition(new LatLng(vehicle.getLat(),vehicle.getLng()));
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }
}
