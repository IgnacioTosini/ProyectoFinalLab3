package Interfaces;

import Excepciones.EleccionIncorrectaException;

public interface IMetodoDePago {

    /**
     * Metodo que recibe un entero para hacer la modularización de los diferentes métodos de pago.
     * @param eleccion
     * @return retorna el precio final en vase el tipo de pago elegido. (clase contenedora de los 3 metodos de pago).
     * @throws EleccionIncorrectaException
     */
    double metodoDePago(int eleccion) throws EleccionIncorrectaException;

    /**
     * Realiza el calculo del precio final si se quiere hacer la pago en efectivo.
     * @return Retorna el valor final según el calculo hecho dentro de la función.
     */
    double pagoEfectivo();
    /**
     * Realiza el calculo del precio final si se quiere hacer la pago con debito.
     * @return Retorna el valor final según el calculo hecho dentro de la función.
     */
    double pagoDebito();

    /**
     * Realiza el calculo del precio final si se quiere hacer la pago con crédito.
     * @return Retorna el valor final según el calculo hecho dentro de la función.
     */
    double pagoCredito();
}
