import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.Usuario;
import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Excepciones.ControladoraUsuario.NombreYApellidoIncorrectoException;
import Interfaces.JsonUtiles;
import Lugares.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        FileInputStream archBinario = null;
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