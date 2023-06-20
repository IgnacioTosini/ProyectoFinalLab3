package Interfaces;

import Lugares.Fecha;

public interface IComprobarFecha {

    /**
     * Metodo el cual un objeto que cuente con un objeto del tipo fecha, pueda comprobar si en su misma lista de fechas ya se encuentra ocupadas las mismas.
     * @param fecha
     * @return
     */
    public boolean validarFecha(Fecha fecha);
}
