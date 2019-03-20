package dev.kevin.app.schoolbustrackerclient.libs;

import org.json.JSONObject;

public interface CallbackWithResponse {
    void execute(JSONObject response);
}
