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

    /**
     * Método que comprueba que si los datos perteneciente a una fecha pasada por 3 enteros es una fecha posible y posterior a la fecha actual.
     * @param año
     * @param mes
     * @param dia
     * @return
     */
    public static boolean validarFecha(int año, int mes, int dia) {
        boolean validacion = false;
        LocalDate localDate = LocalDate.now();
        if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
            if (dia >= 1 && dia <= 31) {
                if (año >= localDate.getYear()) {
                    LocalDate fecha = LocalDate.of(año, mes, dia);
                    if (fecha.isAfter(localDate)) {
                        validacion = true;
                    }
                }
            }
        } else if (mes == 2) {
            if (año >= localDate.getYear()) {
                LocalDate aux = LocalDate.of(año, 1, 1);
                if (aux.isLeapYear()) {
                    if (dia >= 1 && dia <= 29) {
                        LocalDate fecha = LocalDate.of(año, mes, dia);
                        if (fecha.isAfter(localDate)) {
                            validacion = true;
                        }
                    }
                } else if (dia >= 1 && dia <= 28) {
                    LocalDate fecha = LocalDate.of(año, mes, dia);
                    if (fecha.isAfter(localDate)) {
                        validacion = true;
                    }
                }
            }
        } else if (mes > 1 && mes < 13) {
            if (dia >= 1 && dia <= 30) {
                if (año >= localDate.getYear()) {
                    LocalDate fecha = LocalDate.of(año, mes, dia);
                    if (fecha.isAfter(localDate)) {
                        validacion = true;
                    }
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
