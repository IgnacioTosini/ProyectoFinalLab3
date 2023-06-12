package Lugares;

import Cliente.Contraseña;
import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Casa extends Vivienda implements IJson, Comparable {
    private boolean patio;
    private short pisos;

    public Casa(Estado estado, String direccion, short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, int precio, boolean patio, short pisos) {
        super(estado, direccion , ambientes, cantBanios, metrosCuadrados, amueblado, cochera, precio);
        this.patio = patio;
        this.pisos = pisos;
    }

    public Casa() {
        super();
        patio = false;
        pisos = 0;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "patio=" + patio +
                ", pisos=" + pisos +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean validacion = false;
        if(obj != null){
            if(obj instanceof Casa){
                if(getDireccion().equals(((Casa) obj).getDireccion())){
                    validacion = true;
                }
            }
        }


        return validacion;
    }

    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public int compareTo(Object o) {
        int valor = 0;
        if(o != null){
            if(o instanceof Casa){
                valor = getDireccion().compareTo(((Casa) o).getDireccion());
            }
        }
        return valor;
    }

    public boolean isPatio() {
        return patio;
    }

    public short getPisos() {
        return pisos;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();


        return null;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {

    }
}
