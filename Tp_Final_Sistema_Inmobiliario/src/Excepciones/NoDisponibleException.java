package Excepciones;

import Lugares.Fecha;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NoDisponibleException extends Exception{

    String disponibilidad;

    public NoDisponibleException(String message, String disponibilidad) {
        super(message);
        this.disponibilidad = disponibilidad;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }
}
