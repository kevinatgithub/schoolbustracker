package dev.kevin.app.schoolbustrackeradmin.libs;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class ApiManager {

    private static Activity activity;
    private static RequestQueue requestQueue;

    private static void init(Activity _activity){
        activity = _activity;

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(activity);
        }

    }

    public static void execute(final Activity ACTIVITY,final String URL,final int METHOD,@Nullable final JSONObject JSON_OBJECT,final CallbackWithResponse CALLBACK,@Nullable final Callback ON_ERROR){
        init(ACTIVITY);
        requestQueue.add(
                new JsonObjectRequest(METHOD, URL, JSON_OBJECT, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            CALLBACK.execute(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(ON_ERROR != null){
                                ON_ERROR.execute();
                            }
                        }
                    }
                )
        );
    }


}
