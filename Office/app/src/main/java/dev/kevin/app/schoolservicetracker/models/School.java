package dev.kevin.app.schoolservicetracker.models;

public class School {

    private int id;
    private String name, license_no, telephone_no;
    private Geo geo;

    public School(int id, String name, String licenseNo, String telephoneNO, Geo geo) {
        this.id = id;
        this.name = name;
        this.license_no = licenseNo;
        this.telephone_no = telephoneNO;
        this.geo = geo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLicense_no() {
        return license_no;
    }

    public String getTelephone_no() {
        return telephone_no;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public class Geo{
        private String lat;
        private String lng;

        public Geo(String lat, String lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }


}
