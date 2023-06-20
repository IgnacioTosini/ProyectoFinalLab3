package Excepciones.ControladoraUsuario;

public class DniInvalidoException extends Exception{

    /**
             * Excepcion que se lanza cuando el DNI es invalido
             * @param message Mensaje que se mostrara por pantalla
             */

    public DniInvalidoException(String message) {
        super(message);
    }
}
