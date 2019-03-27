package dev.kevin.app.schoolbustrackerclient.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class Session {

    private SharedPreferences prefs;
    private Gson gson;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    public void set(String key, String value){
        prefs.edit().putString(key,value).commit();
    }

    public String get(String key, String defValue){
        String value = prefs.getString(key,"");
        if(value.equals("")){
            return defValue;
        }
        return value;
    }

    public void delete(String key){
        prefs.edit().remove(key).commit();
    }

}
