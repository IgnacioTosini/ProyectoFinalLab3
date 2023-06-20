package Excepciones.Contraseña;

public class CantNumException extends Exception{

    /**
             * Excepcion de la contraseña que tiene en cuenta que se cumplan la cantidad minima de numeros
             * @param message Mensaje que se mostrara por pantalla
             */

    public CantNumException(String message) {
        super(message);
    }
}
