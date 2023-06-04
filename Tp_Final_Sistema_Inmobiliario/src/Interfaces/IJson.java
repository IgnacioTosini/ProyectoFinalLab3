package Interfaces;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IJson {

    JSONObject toJsonObj();

    JSONArray toJsonArray();

    void fromJsonObj(JSONObject obj);

    void fromJsonArray(JSONArray array);

}
