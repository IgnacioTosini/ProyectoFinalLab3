package Interfaces;

import Excepciones.EleccionIncorrectaException;

public interface IMetodoDePago {

    double metodoDePago(int eleccion) throws EleccionIncorrectaException;

    double pagoEfectivo();
    double pagoDebito();
    double pagoCredito();
}
