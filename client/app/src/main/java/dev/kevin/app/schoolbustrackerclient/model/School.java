package dev.kevin.app.schoolbustrackerclient.model;

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

    public String getName() {
        return name;
    }

    public Geo getGeo() {
        return geo;
    }

    public class Geo{
        private String lat,lng;

        public Geo(String lat, String lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }
    }
}
