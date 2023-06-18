import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.Usuario;
import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Lugares.Departamento;
import Lugares.Casa;
import Lugares.Estado;

public class Main {
    public static void main(String[] args) {
        Mail mail = new Mail("admin@gmail.com");
        Contraseña contraseña = new Contraseña("Admin123");
        Mail mail2 = new Mail("joaquin@gmail.com");
        Contraseña contraseña2 = new Contraseña("Joaco123");

        Departamento departamento = new Departamento(Estado.EnAlquiler, "Colon 1200", (short)3,(short)2, 60, true, false, 12000.0, "8A", "A la calle");
        Casa casa = new Casa(Estado.EnAlquiler, "Colon 1000", (short)1,(short)5, 30, true, false, 10000.0, true, (short)3);
        //Cochera cochera = new Cochera();
        Usuario admin = new Usuario("admin",contraseña,"39769347",mail,26,false);
        Usuario usuario = new Usuario("joaquin",contraseña2,"39769347",mail2,26,false);
        Inmobiliaria inmobiliaria = new Inmobiliaria("Los de la UTN", "El puerto", "223641889", "utnmardel@gmail.com");
        inmobiliaria.agregarUsuario(admin);
        inmobiliaria.agregarUsuario(usuario);

        inmobiliaria.agregar(casa);
        inmobiliaria.agregar(departamento);

        ControladoraUsuario.menu(inmobiliaria);
    }
}