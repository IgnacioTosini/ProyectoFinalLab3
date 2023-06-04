package Cliente;

import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Usuario implements IJson {

    private String nombreYApellido;
    private Contraseña contraseña;
    private String dni;
    private String mail;
    private int edad;
    private ArrayList<String> historial;

    public Usuario(String nombreYApellido, Contraseña contraseña, String dni, String mail, int edad) {
        this.nombreYApellido = nombreYApellido;
        this.contraseña = contraseña;
        this.dni = dni;
        this.mail = mail;
        this.edad = edad;
    }


    public boolean encontrarPorNombre(String nombre) {
        boolean validacion = false;
        if (nombreYApellido.equalsIgnoreCase(nombre)) {
            validacion = true;
        }
        return validacion;
    }

    public boolean encontrarPorDni(String dni) {
        boolean validacion = false;
        if (this.dni.equalsIgnoreCase(dni)) {
            validacion = true;
        }
        return validacion;
    }

    public boolean encontrarPorMail(String mail) {
        boolean validacion = false;
        if (this.mail.equalsIgnoreCase(mail)) {
            validacion = true;
        }
        return validacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return edad == usuario.edad && Objects.equals(nombreYApellido, usuario.nombreYApellido) && Objects.equals(contraseña, usuario.contraseña) && Objects.equals(dni, usuario.dni) && Objects.equals(mail, usuario.mail) && Objects.equals(historial, usuario.historial);
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreYApellido='" + nombreYApellido + '\'' +
                ", contraseña=" + contraseña +
                ", dni='" + dni + '\'' +
                ", mail='" + mail + '\'' +
                ", edad=" + edad +
                ", historial=" + historial +
                '}';
    }

    @Override
    public JSONObject toJsonObj() {
        return null;
    }

    @Override
    public JSONArray toJsonArray() {
        return null;
    }

    @Override
    public void fromJsonObj(JSONObject obj) {

    }

    @Override
    public void fromJsonArray(JSONArray array) {

    }
}
