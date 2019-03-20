package dev.kevin.app.schoolbustrackeradmin.models;

public class Bus {

    private String bus_no;
    private String driver;
    private double lat;
    private double lng;

    public Bus(String bus_no, String driver, double lat, double lng) {
        this.bus_no = bus_no;
        this.driver = driver;
        this.lat = lat;
        this.lng = lng;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
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
}
