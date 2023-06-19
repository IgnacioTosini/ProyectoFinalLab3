import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.Usuario;
import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Lugares.Departamento;
import Lugares.Casa;
import Lugares.Estado;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {

        String arch = JsonUtiles.leer("inmobiliaria");
        try {
            JSONObject jsonObject = new JSONObject(arch);
            Inmobiliaria inmobiliaria = null;
            inmobiliaria.fromJsonObj(jsonObject);

            ControladoraUsuario.menu(inmobiliaria);

            JsonUtiles.grabar(inmobiliaria.toJsonObj(), "inmobiliaria");
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


    }
}