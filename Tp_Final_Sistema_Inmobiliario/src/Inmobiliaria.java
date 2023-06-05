import Cliente.Usuario;
import Interfaces.IJson;
import Lugares.Cochera;
import Lugares.Vivienda;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeSet;

public class Inmobiliaria implements IJson {
    private TreeSet<Vivienda> viviendas; //(Ord por precio)
    private TreeSet<Cochera> cocheras; //(Ord por precio)
    private HashMap<String, Usuario> usuarios;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;

    public Inmobiliaria(String nombre, String direccion, String telefono, String correo) {
        this.viviendas = new TreeSet<>();
        this.cocheras = new TreeSet<>();
        this.usuarios = new HashMap<>();
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }

    @Override
    public JSONObject toJsonObj() {
        return null;
    }


    @Override
    public void fromJsonObj(JSONObject obj) {

    }

    public Usuario buscarUsuario(String nombre){

        return usuarios.get(nombre);
    }

    public void agregarUsuario(Usuario usuario){
        usuarios.put(usuario.getNombreYApellido(), usuario);
    }

}
