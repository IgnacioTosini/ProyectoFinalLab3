package Lugares;

import Cliente.Contraseña;
import Controladores.ControladoraInmobiliaria;
import Excepciones.EleccionIncorrectaException;
import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Casa extends Vivienda {
    private boolean patio;
    private short pisos;

    public Casa(Estado estado, String direccion, short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, double precio, boolean patio, short pisos) {
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

    private void setPatio(boolean patio) {
        this.patio = patio;
    }

    private void setPisos(short pisos) {
        this.pisos = pisos;
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
        jsonObject.put("patio", isPatio());
        jsonObject.put("pisos", getPisos());

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
        setCantBanios((short) obj.getInt("cantBanios"));
        setMetrosCuadrados(obj.getInt("metrosCuadrados"));
        setAmueblado(obj.getBoolean("amueblado"));
        setCochera(obj.getBoolean("cochera"));
        setPrecio(obj.getInt("precio"));
        setPatio(obj.getBoolean("patio"));
        setPisos((short) obj.getInt("pisos"));

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
        boolean seguir = true;
        double valorFinal = 0;
        while(seguir){
            try {
                int cantCuotas = ControladoraInmobiliaria.cantCuotas();
                valorFinal = getPrecio() + (getPrecio()*0.03)*cantCuotas;
                seguir = false;
            } catch (EleccionIncorrectaException e) {
                throw new RuntimeException(e);
            }
        }

        return valorFinal;
    }
}
