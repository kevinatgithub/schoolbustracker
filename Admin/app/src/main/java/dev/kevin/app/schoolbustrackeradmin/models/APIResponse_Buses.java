package dev.kevin.app.schoolbustrackeradmin.models;

import java.util.ArrayList;

public class APIResponse_Buses {

    private String status;
    private ArrayList<Bus> buses;

    public APIResponse_Buses(String status, ArrayList<Bus> buses) {
        this.status = status;
        this.buses = buses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Bus> getBuses() {
        return buses;
    }

    public void setBuses(ArrayList<Bus> buses) {
        this.buses = buses;
    }
}
