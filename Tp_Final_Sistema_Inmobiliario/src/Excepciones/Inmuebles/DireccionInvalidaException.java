package Excepciones.Inmuebles;

public class DireccionInvalidaException extends Exception{

    /**
                 * Excepcion que se lanza cuando la direccion es invalida
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public DireccionInvalidaException(String message) {
        super(message);
    }
}
