package Lugares;

import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class Fecha implements IJson {

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    public Fecha(LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public Fecha() {
        fechaSalida = null;
        fechaEntrada = null;
    }

    /**
     * Función para validar las fechas de alquileres, y ver si se encuentra disponible.
     *
     * @param fecha
     * @return true si esta disponible y false esta ocupado.
     */
    public boolean comprobarFecha(Fecha fecha) {
        boolean validacion = false;
        if (fecha.fechaEntrada.isBefore(this.fechaEntrada) && fecha.fechaSalida.isBefore(this.fechaEntrada)) {
            validacion = true;
        } else if (fecha.fechaEntrada.isAfter(this.fechaSalida) && fecha.fechaSalida.isAfter(this.fechaSalida)) {
            validacion = true;
        }
        return validacion;
    }

    public static boolean validarFecha(LocalDate fecha) {
        boolean validacion = false;
        LocalDate localDate = LocalDate.now();

        if (fecha.isAfter(localDate)) {
            if (fecha.getMonthValue() == 1 || fecha.getMonthValue() == 3 || fecha.getMonthValue() == 5 || fecha.getMonthValue() == 7 || fecha.getMonthValue() == 8 || fecha.getMonthValue() == 10 || fecha.getMonthValue() == 12) {
                if (fecha.getDayOfMonth() >= 1 && fecha.getDayOfMonth() <= 31) {
                    validacion = true;
                }
            } else if (fecha.getMonthValue() == 2) {
                if (fecha.isLeapYear()) {
                    if (fecha.getDayOfMonth() >= 1 && fecha.getDayOfMonth() <= 29) {
                        validacion = true;
                    }
                } else {
                    if (fecha.getDayOfMonth() >= 1 && fecha.getDayOfMonth() <= 28) {
                        validacion = true;
                    }
                }
                if (fecha.getDayOfMonth() >= 1 && fecha.getDayOfMonth() <= 28) {
                    validacion = true;
                }
            } else if (fecha.getMonthValue() > 1 && fecha.getMonthValue() < 13) {
                if (fecha.getDayOfMonth() >= 1 && fecha.getDayOfMonth() <= 30) {
                    validacion = true;
                }
            }
        }

        return validacion;
    }

    public int hashCode() {
        return 1;
    }


    @Override
    public String toString() {
        return "Fecha{" +
                "fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                '}';
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    private void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    private void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        int diaEntrada = fechaEntrada.getDayOfMonth();
        int mesEntrada = fechaEntrada.getMonthValue();
        int añoEntrada = fechaEntrada.getYear();

        int diaSalida = fechaSalida.getDayOfMonth();
        int mesSalida = fechaSalida.getMonthValue();
        int añoSalida = fechaSalida.getYear();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("diaEntrada", diaEntrada);
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
        LocalDate fechaEntradaAux = LocalDate.of(añoEntrada, mesEntrada, diaEntrada);
        setFechaEntrada(fechaEntradaAux);


        int diaSalida = obj.getInt("diaSalida");
        int mesSalida = obj.getInt("mesSalida");
        int añoSalida = obj.getInt("añoSalida");
        LocalDate fechaSalidaAux = LocalDate.of(añoSalida, mesSalida, diaSalida);
        setFechaSalida(fechaSalidaAux);

    }
}
