import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Interfaces.JsonUtiles;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        String arch = JsonUtiles.leer("inmobiliaria");
        try {
            JSONObject jsonObject = new JSONObject(arch);
            Inmobiliaria inmobiliaria = new Inmobiliaria();
            inmobiliaria.fromJsonObj(jsonObject);

            ControladoraUsuario.menu(inmobiliaria);

        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
    }
}