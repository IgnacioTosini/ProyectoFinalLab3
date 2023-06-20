package Interfaces;

public interface IBuscar {
    /**
     * Se le pasa una dirección para buscar y saber si se encuentra un inmueble.
     * @param direccion
     * @return boolean
     */
    boolean buscar(String direccion);
}
