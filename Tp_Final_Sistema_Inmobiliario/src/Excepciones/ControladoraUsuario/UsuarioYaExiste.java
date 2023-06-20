package Excepciones.ControladoraUsuario;

public class UsuarioYaExiste extends Exception{

    /**
                 * Excepcion que se lanza cuando el usuario ya existe
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public UsuarioYaExiste(String message) {
        super(message);
    }
}
