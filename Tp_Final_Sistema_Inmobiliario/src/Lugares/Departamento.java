package Lugares;

import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

public class Departamento extends Vivienda implements IJson, Comparable {
    private String nroPiso;
    private String disposicion;

    public Departamento(Estado estado, String direccion, boolean vendido , short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, int precio, String nroPiso, String disposicion) {
        super(estado, direccion, ambientes, cantBanios, metrosCuadrados, amueblado, cochera, precio);
        this.nroPiso = nroPiso;
        this.disposicion = disposicion;
    }

    public Departamento() {
        super();
        nroPiso = "";
        disposicion = "";
    }

    @Override
    public boolean equals(Object obj) {
        boolean validacion = false;
        if(obj != null){
            if(obj instanceof Departamento){
                if(getDireccion().equals(((Departamento) obj).getDireccion())){
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
            if(o instanceof Departamento){
                valor = getDireccion().compareTo(((Departamento) o).getDireccion());
            }
        }
        return valor;
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "nroPiso='" + nroPiso + '\'' +
                ", disposicion='" + disposicion + '\'' +
                "} " + super.toString();
    }

    public String getNroPiso() {
        return nroPiso;
    }

    public String getDisposicion() {
        return disposicion;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        return null;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {

    }
}
