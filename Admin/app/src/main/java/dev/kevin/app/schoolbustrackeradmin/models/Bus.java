package dev.kevin.app.schoolbustrackeradmin.models;

public class Bus {

    private String bus_no;
    private String driver;

    public Bus(String bus_no, String driver) {
        this.bus_no = bus_no;
        this.driver = driver;
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
}
