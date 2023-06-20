package Excepciones;

public class LugarExistenteException extends Exception {

    /**
     * Excepción cuando se quiere agregar un inmueble y ya esta existe no deja cargarlo.
     * @param message Mensaje que se mostrara por pantalla
     */
    public LugarExistenteException(String message) {
        super(message);
    }
}
