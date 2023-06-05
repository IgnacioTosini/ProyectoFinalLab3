package Interfaces;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface IJson {

    JSONObject toJsonObj() throws JSONException;

    void fromJsonObj(JSONObject obj) throws JSONException;


}
