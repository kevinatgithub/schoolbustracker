package app.kevin.dev.schoolservicetrackerparent.lib;

public class AppConstants {
    public static final String DOMAIN = "http://test.nbbnets.net/track3r.php?endpoint=";

    public static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1Ijoia2V2c2F0bWFwYm94IiwiYSI6ImNqdDgzNmllNTA0bDY0NG52OGQ0NjlhNjAifQ.uJK-QHLEqy3jGBNpXJQuHQ";

    public static final String MAP_QUEST_ACCESS_TOKEN = "QIXjmS5OpOigRmhkHBvu6k4LuGSNCB4V";

    public static final String MAP_QUEST_DIRECTION_URL = "https://www.mapquestapi.com/directions/v2/route?key="+MAP_QUEST_ACCESS_TOKEN+"&from=[POINT_A_LAT_LNG]&to=[POINT_B_LAT_LNG]&outFormat=json&ambiguities=ignore&routeType=fastest&doReverseGeocode=false&enhancedNarrative=false&avoidTimedConditions=false";

}
