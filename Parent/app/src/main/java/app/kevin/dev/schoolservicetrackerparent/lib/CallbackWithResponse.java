package app.kevin.dev.schoolservicetrackerparent.lib;

import org.json.JSONObject;

public interface CallbackWithResponse {
    void execute(JSONObject response);
}
