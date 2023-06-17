package Controladores;

import Excepciones.EleccionIncorrectaException;
import Excepciones.NoDisponibleException;

import java.util.Scanner;

public class ControladoraInmobiliaria {

    static Scanner teclado = new Scanner(System.in);
    public static int eleccionMetodoDePago() throws EleccionIncorrectaException {
        System.out.println("¿Cómo desea Pagar?"+ '\n' + "1. Efectivo" +
                "2. Targeta Debito \n" +
                "3. Targeta Credito");
        int eleccion = teclado.nextInt();
        if(eleccion >4 && eleccion<1){
            throw new EleccionIncorrectaException("La opción elegida es incorrecta");
        }
        return eleccion;
    }

    public static int cantCuotas() throws EleccionIncorrectaException { //Preguntar si el metodo de pago sin controlar ingresos de momento esta bien, ya que solo se hace la facturacion
        int eleccion = 0;
        boolean seguir = true;
        int cantCuotas =1; //Es uno cosa que la cantidad de cuotas por defecto es 1(valor del inmueble).
        while(seguir){
            System.out.println("¿Cuantas cuotas lo desea hacer? \n" +
                    "1. 6 cuotas\n" +
                    "2. 12 cuotas\n" +
                    "3. 24 cuotas");
            eleccion = teclado.nextInt();
            if(eleccion == 1){
                cantCuotas = 6;
                seguir = false;
            } else if (eleccion == 2) {
                cantCuotas = 12;
                seguir = false;
            } else if (eleccion == 3) {
                cantCuotas = 24;
                seguir = false;
            }else {
                System.out.println("La elección es incorrecta");
            }
        }

        return cantCuotas;
    }


}
