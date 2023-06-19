import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.Usuario;
import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Interfaces.JsonUtiles;
import Lugares.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {

        String arch = JsonUtiles.leer("inmobiliaria");
        try {
            JSONObject jsonObject = new JSONObject(arch);
            Inmobiliaria inmobiliaria = new Inmobiliaria();
            inmobiliaria.fromJsonObj(jsonObject);
           /* Mail mail = new Mail("admin@gmail.com");
            Contraseña contraseña = new Contraseña("Admin123");
            Mail mail2 = new Mail("joaquin@gmail.com");
            Contraseña contraseña2 = new Contraseña("Joaco123");

            Departamento departamento = new Departamento(Estado.EnAlquiler, "Colon 1200", (short)3,(short)2, 60, true, false, 12000.0, "8A", "A la calle");
            Casa casa = new Casa(Estado.EnAlquiler, "Colon 1000", (short)1, (short)5, 30, true, false, 10000.0, true, (short)3);
            Cochera cochera = new Cochera("oli 432", Estado.Baja, (short) 3, (short) 4, "Ascensor", 2000.0);
            Local local = new Local("cas 123", Estado.EnVenta, (short)3, true, 123.0);
            Usuario admin = new Usuario("admin",contraseña,"39769347",mail,26,false);
            Usuario usuario = new Usuario("joaquin",contraseña2,"39769347",mail2,26,false);
            Inmobiliaria inmobiliaria = new Inmobiliaria("Los de la UTN", "El puerto", "223641889", "utnmardel@gmail.com");
            inmobiliaria.agregarUsuario(admin);
            inmobiliaria.agregarUsuario(usuario);

            inmobiliaria.agregar(casa);
            inmobiliaria.agregar(departamento);
            inmobiliaria.agregar(local);
            inmobiliaria.agregar(cochera);*/

            ControladoraUsuario.menu(inmobiliaria);

            JsonUtiles.grabar(inmobiliaria.toJsonObj(), "inmobiliaria");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


    }
}