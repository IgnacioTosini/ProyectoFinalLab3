package Lugares;

import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class Fecha implements IJson {

    private Calendar fechaEntrada;
    private Calendar fechaSalida;

    public Fecha(Calendar fechaEntrada, Calendar fechaSalida) {
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }
    public Fecha(){
        fechaSalida = null;
        fechaEntrada = null;
    }


    /**
     * Función para validar las fechas de alquileres, y ver si se encuentra disponible.
     * @param fecha
     * @return true si esta disponible y false esta ocupado.
     */
    public boolean comprobarFecha(Fecha fecha){
        boolean validacion = false;

        if(fecha.fechaEntrada.before(fechaEntrada) && fecha.fechaSalida.before(fechaEntrada)){
            validacion = true;
        } else if (fecha.fechaEntrada.after(fechaSalida) && fecha.fechaSalida.after(fechaSalida)) {
            validacion = true;
        }
        return validacion;
    }


    @Override
    public String toString() {
        return "Fecha{" +
                "fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                '}';
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        Date dateEntrada = fechaEntrada.getTime();
        int diaEntrada = dateEntrada.getDay();
        int mesEntrada = dateEntrada.getMonth();
        int añoEntrada = dateEntrada.getYear();

        Date dateSalida = fechaSalida.getTime();
        int diaSalida = dateSalida.getDay();
        int mesSalida = dateSalida.getMonth();
        int añoSalida = dateSalida.getYear();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("diaEntrada", diaEntrada );
        jsonObject.put("mesEntrada", mesEntrada);
        jsonObject.put("añoEntrada", añoEntrada);

        jsonObject.put("diaSalida", diaSalida);
        jsonObject.put("mesSalida", mesSalida);
        jsonObject.put("añoSalida", añoSalida);
        return jsonObject;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {
        int diaEntrada = obj.getInt("diaEntrada");
        int mesEntrada = obj.getInt("mesEntrada");
        int añoEntrada = obj.getInt("añoEntrada");

        fechaEntrada.set(añoEntrada,mesEntrada,diaEntrada);

        int diaSalida = obj.getInt("diaSalida");
        int mesSalida = obj.getInt("mesSalida");
        int añoSalida = obj.getInt("añoSalida");

        fechaSalida.set(añoSalida,mesSalida,diaSalida);
    }
}
