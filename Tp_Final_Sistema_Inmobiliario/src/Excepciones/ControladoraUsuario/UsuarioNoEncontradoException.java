package Excepciones.ControladoraUsuario;

import java.util.Scanner;

public class UsuarioNoEncontradoException extends Exception{

    public UsuarioNoEncontradoException(String message) {
        super(message);
    }

}
