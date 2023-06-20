package Excepciones.ControladoraUsuario;

public class NombreYApellidoIncorrectoException extends Exception{

    /**
                 * Excepcion que se lanza cuando el nombre y apellido es invalido
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public NombreYApellidoIncorrectoException(String message) {
        super(message);
    }
}
