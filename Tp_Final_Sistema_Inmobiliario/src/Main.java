import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;

public class Main {
    public static void main(String[] args) {

        Inmobiliaria inmobiliaria = new Inmobiliaria("Los de la UTN", "El puerto", "223641889", "utnmardel@gmail.com");
        ControladoraUsuario controladoraUsuario = new ControladoraUsuario();

        controladoraUsuario.menu(inmobiliaria);
    }
}