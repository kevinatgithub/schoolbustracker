package app.kevin.dev.schoolservicetrackerparent.lib;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
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

    public static void execute(final Activity ACTIVITY,final String URL,@Nullable final CallbackWithResponse CALLBACK){
        init(ACTIVITY);
        requestQueue.add(
                new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(CALLBACK != null){
                            CALLBACK.execute(response);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ACTIVITY, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }


}
