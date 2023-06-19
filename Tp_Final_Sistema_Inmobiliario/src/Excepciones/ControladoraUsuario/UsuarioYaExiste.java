package Excepciones.ControladoraUsuario;

public class UsuarioYaExiste extends Exception{

    public UsuarioYaExiste() {
    }

    public UsuarioYaExiste(String message) {
        super(message);
    }
}
