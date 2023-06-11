package Excepciones;

public class TotalDigitosException extends Exception{

    private int cantDigitos;
    public TotalDigitosException(String message, int cantDigitos) {
        super(message);
        this.cantDigitos = cantDigitos;
    }
}
