package dev.kevin.app.schoolservicetracker.models;

public class GeocodingResult {

    private Location[] locations;

    public GeocodingResult(Location[] locations) {
        this.locations = locations;
    }

    public Location[] getLocations() {
        return locations;
    }

    public class Location{
        private LatLng latLng;

        public Location(LatLng latLng) {
            this.latLng = latLng;
        }

        public LatLng getLatLng() {
            return latLng;
        }
    }

    public class LatLng{
        private double lat;
        private double lng;

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
