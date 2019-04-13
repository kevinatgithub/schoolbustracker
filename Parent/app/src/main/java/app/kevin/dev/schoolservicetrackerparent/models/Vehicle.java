package app.kevin.dev.schoolservicetrackerparent.models;

public class Vehicle {

    private int id,reassign;
    private String plate_no, model, driver, contact_no,status;
    private double lat,lng;
    private String enable_parent_tracking;

    public Vehicle(int id, int reassign, String plate_no, String model, String driver, String contact_no, String status, double lat, double lng, String enable_parent_tracking) {
        this.id = id;
        this.reassign = reassign;
        this.plate_no = plate_no;
        this.model = model;
        this.driver = driver;
        this.contact_no = contact_no;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
        this.enable_parent_tracking = enable_parent_tracking;
    }

    public int getId() {
        return id;
    }

    public int getReassign() {
        return reassign;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public String getModel() {
        return model;
    }

    public String getDriver() {
        return driver;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getStatus() {
        return status;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getEnable_parent_tracking() {
        return enable_parent_tracking;
    }
}
