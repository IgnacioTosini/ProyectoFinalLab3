package Excepciones;

public class NoDisponibleException extends Exception{
    
    /**
        * Excepcion que se lanza cuando las fechas de un inmueble estan tomadas
        * @param message Mensaje que se mostrara por pantalla
        * @param disponibilidad String con las fechas ya reservadas del inmueble
        */
        

    String disponibilidad;

    public NoDisponibleException(String message, String disponibilidad) {
        super(message);
        this.disponibilidad = disponibilidad;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }
}
