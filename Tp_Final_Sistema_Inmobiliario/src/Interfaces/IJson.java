package Interfaces;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface IJson {

    /**
     * Método utilizado para crear un JSONObject del objeto implementado.
     * @return Retorna un JSONObject
     * @throws JSONException
     */
    JSONObject toJsonObj() throws JSONException;

    /**
     * Recibe un JSONObject para poder cargar el objeto implementado
     * @param obj
     * @throws JSONException
     */
    void fromJsonObj(JSONObject obj) throws JSONException;


}
