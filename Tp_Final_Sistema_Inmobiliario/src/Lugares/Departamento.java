package Lugares;

import Controladores.ControladoraInmobiliaria;
import Excepciones.EleccionIncorrectaException;
import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Departamento extends Vivienda implements Comparable {
    private String nroPiso;
    private String disposicion;

    public Departamento(Estado estado, String direccion , short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, double precio, String nroPiso, String disposicion) {
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

    public void setNroPiso(String nroPiso) {
        this.nroPiso = nroPiso;
    }

    public void setDisposicion(String disposicion) {
        this.disposicion = disposicion;
    }


    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("estado",getEstado().name());
        jsonObject.put("direccion", getDireccion());
        jsonObject.put("ambientes", getAmbientes());
        jsonObject.put("canBanios", getCantBanios());
        jsonObject.put("metrosCuadrados", getMetrosCuadrados());
        jsonObject.put("amueblado", isAmueblado());
        jsonObject.put("cochera", isCochera());
        jsonObject.put("precio", getPrecio());
        jsonObject.put("nroPiso", nroPiso);
        jsonObject.put("disposicion", disposicion);



        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i<cantDeFechas();i++){
            jsonArray.put(buscarFecha(i).toJsonObj());
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
        setAmbientes((short) obj.getInt("ambientes"));
        setCantBanios((short) obj.getInt("canBanios"));
        setMetrosCuadrados(obj.getInt("metrosCuadrados"));
        setAmueblado(obj.getBoolean("amueblado"));
        setCochera(obj.getBoolean("cochera"));
        setPrecio(obj.getInt("precio"));
        setNroPiso(obj.getString("nroPiso"));
        setDisposicion(obj.getString("disposicion"));


        JSONArray jsonArray = obj.getJSONArray("disponibilidad");

        Fecha fecha = new Fecha();
        for(int i = 0; i<jsonArray.length();i++){
            fecha.fromJsonObj((JSONObject) jsonArray.get(i));
            agregarDisponibilidad(fecha);
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
        double valorFinal = getPrecio()- getPrecio()*0.05;

        return valorFinal;
    }

    @Override
    public double pagoDebito() {

        return getPrecio();
    }

    @Override
    public double pagoCredito() {
        double valorFinal = 0;
                valorFinal = getPrecio() + (getPrecio()*0.03);

        return valorFinal;
    }
}
