package Excepciones.ControladoraUsuario;

public class EdadInvalidadException extends Exception{

    /**
                 * Excepcion que se lanza cuando la edad es invalida
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public EdadInvalidadException(String message) {
        super(message);
    }
}
