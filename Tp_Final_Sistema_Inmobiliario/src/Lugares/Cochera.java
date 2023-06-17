package Lugares;

import Controladores.ControladoraInmobiliaria;
import Excepciones.EleccionIncorrectaException;
import Interfaces.IBuscar;
import Interfaces.IComprobarFecha;
import Interfaces.IJson;
import Interfaces.IMetodoDePago;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Cochera implements IComprobarFecha, IJson, Comparable, IMetodoDePago, IBuscar {
    private ArrayList<Fecha> disponibilidad;
    private String direccion;
    private Estado estado;
    private short piso;
    private short posicion;
    private String medioDeAcceso;
    private double precio;

    public Cochera(String direccion, Estado estado, short piso, short posicion, String medioDeAcceso, double precio) {
        disponibilidad = new ArrayList<>();
        this.direccion = direccion;
        this.estado = estado;
        this.piso = piso;
        this.posicion = posicion;
        this.medioDeAcceso = medioDeAcceso;
        this.precio = precio;
    }

    public Cochera() {
        disponibilidad  = new ArrayList<>();
        direccion = "";
        estado = null;
        piso = 0;
        posicion = 0;
        medioDeAcceso = "";
        precio = 0;
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
                ", medioDeAcceso='" + medioDeAcceso + '\'' +
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

    public String getMedioDeAcceso() {
        return medioDeAcceso;
    }
    public double getPrecio() {
        return precio;
    }

    public void agregarDisponibilidad(Fecha fecha){
        disponibilidad.add(fecha);
    }

    private void setDisponibilidad(ArrayList<Fecha> disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    private void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    private void setEstado(Estado estado) {
        this.estado = estado;
    }

    private void setPiso(short piso) {
        this.piso = piso;
    }

    private void setPosicion(short posicion) {
        this.posicion = posicion;
    }

    private void setMedioDeAcceso(String medioDeAcceso) {
        this.medioDeAcceso = medioDeAcceso;
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("estado", estado.name());
        jsonObject.put("direccion", direccion);
        jsonObject.put("piso", piso);
        jsonObject.put("posicion", posicion);
        jsonObject.put("mediosDeAcceso", medioDeAcceso);


        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i<disponibilidad.size();i++){
            jsonArray.put(disponibilidad.get(i).toJsonObj());
        }
        jsonObject.put("disponibilidad", jsonArray);

        return jsonObject;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {
        String estado = obj.getString("estado");
        switch (estado) {
            case "EnVenta" -> setEstado(Estado.EnVenta);
            case "EnAlquiler" -> setEstado(Estado.EnAlquiler);
            case "Baja" -> setEstado(Estado.Baja);
        }

        setDireccion(obj.getString("direccion"));
        setPiso((short) obj.getInt("piso"));
        setPosicion((short) obj.getInt("posicion"));
        setMedioDeAcceso(obj.getString("medioDeAcceso"));

        JSONArray jsonArray = obj.getJSONArray("disponibilidad");

        Fecha fecha = new Fecha();
        for(int i = 0; i<jsonArray.length();i++){
            fecha.fromJsonObj((JSONObject) jsonArray.get(i));
            disponibilidad.add(fecha);
        }
    }

    @Override
    public double metodoDePago(int eleccion) throws EleccionIncorrectaException {
        double valorFinal = 0;
        if(eleccion == 1){
            valorFinal = pagoEfectivo();
        } else if (eleccion == 2) {
            valorFinal = pagoDebito();
        } else if (eleccion == 3) {
            valorFinal = pagoCredito();
        }else{
            throw new EleccionIncorrectaException("El valor ingresado es incorrecto");
        }

        return valorFinal;
    }

    @Override
    public double pagoEfectivo() {
        double valorFinal = precio- precio*0.2;

        return valorFinal;
    }

    @Override
    public double pagoDebito() {

        return precio;
    }

    @Override
    public double pagoCredito() {
        boolean seguir = true;
        double valorFinal = 0;
        while(seguir){
            try {
                int cantCuotas = ControladoraInmobiliaria.cantCuotas();
                valorFinal = precio + (precio*0.1)*cantCuotas;
                seguir = false;
            } catch (EleccionIncorrectaException e) {
                seguir = true;
            }
        }

        return valorFinal;
    }

    @Override
    public boolean buscar(String direccion) {
        boolean encontrado = false;
        if (this.direccion.equalsIgnoreCase(direccion)){
            encontrado = true;
        }
        return encontrado;
    }
}
