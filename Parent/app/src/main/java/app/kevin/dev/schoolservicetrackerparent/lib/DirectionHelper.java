package app.kevin.dev.schoolservicetrackerparent.lib;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONObject;

public class DirectionHelper {

    private static Gson gson = new Gson();

    public static void getDistance(Activity activity, double pointA_lat, double pointA_lng, double pointB_lat, double pointB_lng, final DirectionHelperCallback directionHelperCallback){
        String url = AppConstants.MAP_QUEST_DIRECTION_URL;
        String pointA = String.valueOf(pointA_lat) +"," + String.valueOf(pointA_lng);
        String pointB = String.valueOf(pointB_lat) +"," + String.valueOf(pointB_lng);

        url = url.replace("[POINT_A_LAT_LNG]",AppHelper.urlEncode(pointA));
        url = url.replace("[POINT_B_LAT_LNG]",AppHelper.urlEncode(pointB));

        ApiManager.execute(activity, url, new CallbackWithResponse() {
            @Override
            public void execute(JSONObject response) {
                parseResponse(response,directionHelperCallback);
            }
        });
    }

    private static void parseResponse(JSONObject response,DirectionHelperCallback callback) {
        ApiCallback apiCallback = gson.fromJson(response.toString(),ApiCallback.class);
        if(apiCallback != null){
            callback.execute(apiCallback.getRoute());
        }else{
            callback.execute(null);
        }
    }

    public interface DirectionHelperCallback {
        void execute(Route route);
    }

    public class ApiCallback{
        private Route route;

        public ApiCallback(Route route) {
            this.route = route;
        }

        public Route getRoute() {
            return route;
        }
    }

    public class Route{
        private String formattedTime;
        private double distance;
        private int time;

        public Route(String formattedTime, double distance, int time) {
            this.formattedTime = formattedTime;
            this.distance = distance;
            this.time = time;
        }

        public String getFormattedTime() {
            return formattedTime;
        }

        public double getDistance() {
            return distance;
        }

        public int getTime() {
            return time;
        }
    }
}
