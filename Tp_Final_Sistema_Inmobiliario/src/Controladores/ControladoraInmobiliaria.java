package Controladores;

import Excepciones.EleccionIncorrectaException;

import java.util.Scanner;

public class ControladoraInmobiliaria {

    static Scanner teclado = new Scanner(System.in);

    public static int eleccionMetodoDePago() throws EleccionIncorrectaException {
        System.out.println("¿Cómo desea Pagar?" + '\n' + "1. Efectivo" +
                "\n2. Targeta Debito \n" +
                "3. Targeta Credito");
        int eleccion = Integer.parseInt(teclado.nextLine());
        if (eleccion > 4 && eleccion < 1) {
            throw new EleccionIncorrectaException("La opción elegida es incorrecta");
        }
        return eleccion;
    }

}
