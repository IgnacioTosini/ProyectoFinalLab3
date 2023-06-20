package Excepciones.Mail;

public class ArrobaException extends Exception{

    /**
                 * Excepcion que se lanza cuando un mail no tiene arroba
                 * @param message Mensaje que se mostrara por pantalla
                 */

    public ArrobaException(String message) {
        super(message);
    }
}
