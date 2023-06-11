package Lugares;

import java.util.Calendar;

public class Fecha {

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




}
