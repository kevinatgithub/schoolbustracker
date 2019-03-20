package dev.kevin.app.schoolbustrackeradmin.models;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class MapMarkerOptions{
    private Bus bus;
    private MarkerOptions markerOptions;

    public MapMarkerOptions(Bus bus) {
        this.bus = bus;
        markerOptions = new MarkerOptions()
                .position(new LatLng(bus.getLat(),bus.getLng()))
                .setTitle(bus.getBus_no() + " - " + bus.getDriver());
    }

    public void setBus(Bus bus) {
        this.bus = bus;
        markerOptions.setPosition(new LatLng(bus.getLat(),bus.getLng()));
    }

    public Bus getBus() {
        return bus;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }
}
