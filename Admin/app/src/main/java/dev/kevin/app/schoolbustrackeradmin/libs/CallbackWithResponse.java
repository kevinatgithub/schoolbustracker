package dev.kevin.app.schoolbustrackeradmin.libs;

import org.json.JSONObject;

public interface CallbackWithResponse {
    void execute(JSONObject response);
}
