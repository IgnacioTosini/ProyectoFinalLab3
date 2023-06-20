package Excepciones.Mail;

public class PuntoComException extends Exception{

    /**
            * Excepcion que se lanza cuando un mail no tiene ".com"
            * @param message Mensaje que se mostrara por pantalla
            */


    public PuntoComException(String message) {
        super(message);
    }
}
