package Lugares;

import Interfaces.IComprobarFecha;
import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Local implements IComprobarFecha, IJson, Comparable {
    private ArrayList<Fecha> disponibilidad;
    private String direccion;
    private Estado estado;
    private short ambientes;
    private boolean vidriera;
    private boolean vendido;
    private int precio;

    public Local(String direccion, Estado estado, short ambientes, boolean vidriera, boolean vendido, int precio) {
        disponibilidad = new ArrayList<>();
        this.direccion = direccion;
        this.estado = estado;
        this.ambientes = ambientes;
        this.vidriera = vidriera;
        this.vendido = vendido;
        this.precio = precio;
    }

    public Local() {
        disponibilidad = new ArrayList<>();
        direccion = "";
        estado = null;
        ambientes = 0;
        vidriera = false;
        vendido = false;
        precio = 0;
    }



    public String getDireccion() {
        return direccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public short getAmbientes() {
        return ambientes;
    }

    public boolean isVidriera() {
        return vidriera;
    }

    public boolean isVendido() {
        return vendido;
    }

    public int getPrecio() {
        return precio;
    }


    @Override
    public boolean equals(Object obj) {
        boolean validacion = false;
        if(obj != null){
            if(obj instanceof Local){
                if(direccion.equals(((Local) obj).getDireccion())){
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
            if(o instanceof Local){
                valor = direccion.compareTo(((Local) o).getDireccion());
            }
        }
        return valor;
    }


    @Override
    public String toString() {
        return "Local{" +
                "disponibilidad=" + disponibilidad +
                ", direccion='" + direccion + '\'' +
                ", estado=" + estado +
                ", ambientes=" + ambientes +
                ", vidriera=" + vidriera +
                ", vendido=" + vendido +
                ", precio=" + precio +
                '}';
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
