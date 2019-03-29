package dev.kevin.app.schoolservicetracker.libs;

import org.json.JSONObject;

public interface CallbackWithResponse {
    void execute(JSONObject response);
}
