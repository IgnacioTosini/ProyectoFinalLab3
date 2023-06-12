package Lugares;

import Interfaces.IComprobarFecha;
import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cochera implements IComprobarFecha, IJson, Comparable {
    private ArrayList<Fecha> disponibilidad;
    private String direccion;
    private Estado estado;
    private short piso;
    private short posicion;
    private boolean vendido;
    private String medioDeAcceso;
    private boolean ascensor;

    public Cochera(String direccion, Estado estado, short piso, short posicion, boolean vendido, String medioDeAcceso, boolean ascensor) {
        disponibilidad = new ArrayList<>();
        this.direccion = direccion;
        this.estado = estado;
        this.piso = piso;
        this.posicion = posicion;
        this.vendido = vendido;
        this.medioDeAcceso = medioDeAcceso;
        this.ascensor = ascensor;
    }

    public Cochera() {
        disponibilidad  = new ArrayList<>();
        direccion = "";
        estado = null;
        piso = 0;
        posicion = 0;
        vendido = false;
        medioDeAcceso = "";
        ascensor = false;
    }


    @Override
    public boolean equals(Object obj) {
        boolean validacion = false;
        if(obj != null){
            if(obj instanceof Cochera){
                if(direccion.equals(((Cochera) obj).getDireccion())){
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
            if(o instanceof Cochera){
                valor = direccion.compareTo(((Cochera) o).getDireccion());
            }
        }
        return valor;
    }

    @Override
    public String toString() {
        return "Cochera{" +
                "disponibilidad=" + disponibilidad +
                ", direccion='" + direccion + '\'' +
                ", estado=" + estado +
                ", piso=" + piso +
                ", posicion=" + posicion +
                ", vendido=" + vendido +
                ", medioDeAcceso='" + medioDeAcceso + '\'' +
                ", ascensor=" + ascensor +
                '}';
    }

    public String getDireccion() {
        return direccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public short getPiso() {
        return piso;
    }

    public short getPosicion() {
        return posicion;
    }

    public boolean isVendido() {
        return vendido;
    }

    public String getMedioDeAcceso() {
        return medioDeAcceso;
    }

    public boolean isAscensor() {
        return ascensor;
    }


    @Override
    public boolean validarFecha(Fecha fecha) {
        Fecha aux = new Fecha();
        boolean validacion = false;
        for(int i = 0; i<disponibilidad.size(); i++){
            aux = disponibilidad.get(i);
            if(aux.comprobarFecha(fecha)){
                validacion = true;
            }
        }

        return validacion;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        return null;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {

    }
}
