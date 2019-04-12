package app.kevin.dev.schoolservicetrackerparent.lib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AppHelper {

    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}