package Excepciones;

public class EleccionIncorrectaException extends Exception{

    /**
     * Excepción utilizada en situaciones donde hay multiple opciones y se elige una que no es valida.
     * @param message Mensaje que se mostrara por pantalla
     */
    public EleccionIncorrectaException(String message) {
        super(message);
    }
}
