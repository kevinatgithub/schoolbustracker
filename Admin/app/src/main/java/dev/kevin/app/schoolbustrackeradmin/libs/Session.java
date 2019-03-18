package dev.kevin.app.schoolbustrackeradmin.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class Session {

    private static SharedPreferences prefs;
    private static Gson gson;

    private static void init(Context context){
        if(prefs == null){
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if(gson == null){
            gson = new Gson();
        }
    }

    public static void set(Context context, String key, String value){
        init(context);
        prefs.edit().putString(key,value);
    }

    public static String get(Context context,String key, String defValue){
        init(context);
        String value = prefs.getString(key,"");
        if(value.equals("")){
            return defValue;
        }
        return value;
    }

    public static void delete(Context context,String key){
        init(context);
        prefs.edit().remove(key).commit();
    }

}
