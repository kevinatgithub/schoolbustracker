package dev.kevin.app.schoolservicetracker.models;

public class School {

    private int id;
    private String name;
    private Geo geo;

    public School(int id, String name, Geo geo) {
        this.id = id;
        this.name = name;
        this.geo = geo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
