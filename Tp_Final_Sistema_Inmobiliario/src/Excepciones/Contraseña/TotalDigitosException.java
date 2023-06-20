package Excepciones.Contraseña;

public class TotalDigitosException extends Exception{
    
    /**
             * Excepcion de la contraseña que tiene en cuenta que se cumplan la cantidad minima de digitos
             * @param message Mensaje que se mostrara por pantalla
             * @param cantDigitos Cantidad de digitos que se ingreso
             */

    private int cantDigitos;
    public TotalDigitosException(String message, int cantDigitos) {
        super(message);
        this.cantDigitos = cantDigitos;
    }
}
