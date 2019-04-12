package app.kevin.dev.schoolservicetrackerparent.models;

import java.util.ArrayList;

public class ApiResponse {

    private ArrayList<Vehicle> vehicles;

    public ApiResponse(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
}
