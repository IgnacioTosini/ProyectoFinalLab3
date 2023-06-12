package Lugares;

import Controladores.ControladoraInmobiliaria;
import Excepciones.EleccionIncorrectaException;
import Interfaces.IComprobarFecha;
import Interfaces.IJson;
import Interfaces.IMetodoDePago;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Local implements IComprobarFecha, IJson, Comparable, IMetodoDePago {
    private ArrayList<Fecha> disponibilidad;
    private String direccion;
    private Estado estado;
    private short ambientes;
    private boolean vidriera;
    private boolean vendido;
    private double precio;

    public Local(String direccion, Estado estado, short ambientes, boolean vidriera, boolean vendido, double precio) {
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

    public double getPrecio() {
        return precio;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setAmbientes(short ambientes) {
        this.ambientes = ambientes;
    }

    public void setVidriera(boolean vidriera) {
        this.vidriera = vidriera;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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

    public void agregarDisponibilidad(Fecha fecha){
        disponibilidad.add(fecha);
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("estado", estado.name());
        jsonObject.put("direccion", direccion);
        jsonObject.put("ambientes", ambientes);
        jsonObject.put("vidriera", vidriera);
        jsonObject.put("vendido", vendido);
        jsonObject.put("precio", precio);

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
        if(estado.equals("Vendido")){
            setEstado(Estado.Vendido);
        } else if (estado.equals("EnVenta")) {
            setEstado(Estado.EnVenta);
        }else if(estado.equals("EnAlquiler")){
            setEstado(Estado.EnAlquiler);
        }else if(estado.equals("Baja")){
            setEstado(Estado.Baja);
        }


        setDireccion(obj.getString("direccion"));
        setAmbientes((short) obj.getInt("ambientes"));
        setVidriera(obj.getBoolean("vidriera"));
        setVendido(obj.getBoolean("vendido"));
        setPrecio(obj.getDouble("precio"));


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
       double valorFinal = precio- precio*0.1;

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
                valorFinal = precio + (precio*0.02)*cantCuotas;
                seguir = false;
            } catch (EleccionIncorrectaException e) {
                seguir = true;
            }
        }

        return valorFinal;
    }
}
