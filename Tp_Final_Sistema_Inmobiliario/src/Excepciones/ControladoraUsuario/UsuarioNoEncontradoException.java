package Excepciones.ControladoraUsuario;

import java.util.Scanner;

public class UsuarioNoEncontradoException extends Exception{

    /**
                 * Excepcion que se lanza cuando el usuario no se encontro
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }

}
