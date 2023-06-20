package Excepciones.ControladoraUsuario;

public class UsuarioDadoDeBajaException extends Exception{

    /**
                 * Excepcion que se lanza cuando el usuario ingresado fue dado de baja
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public UsuarioDadoDeBajaException(String message) {
        super(message);
    }

}
