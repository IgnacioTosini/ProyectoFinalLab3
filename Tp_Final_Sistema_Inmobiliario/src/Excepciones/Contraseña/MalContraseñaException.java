package Excepciones.Contraseña;

public class MalContraseñaException extends Exception {

    /**
             * Excepcion lanzada cuando la contraseña es incorrecta al intentar loguearse
             * @param message Mensaje que se mostrara por pantalla
             */

    public MalContraseñaException(String message) {
        super(message);
    }
}
