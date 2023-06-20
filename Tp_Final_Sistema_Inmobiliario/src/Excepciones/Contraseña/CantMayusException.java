package Excepciones.Contraseña;

public class CantMayusException extends Exception{
    
    /**
         * Excepcion de la contraseña que tiene en cuenta que se cumplan la cantidad minima de mayusculas
         * @param message Mensaje que se mostrara por pantalla
         */
    
    public CantMayusException(String message) {
        super(message);

    }
}
