package Cliente;

import Empresa.Factura;
import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class Usuario implements IJson, Serializable, Comparable {
    private String nombreYApellido;
    private Contraseña contraseña;
    private String dni;
    private Mail mail;
    private int edad;
    private ArrayList<String> historial;
    private HashMap<Integer, Factura> facturas;
    private boolean estado;

    public Usuario(String nombreYApellido, Contraseña contraseña, String dni, Mail mail, int edad, boolean estado) {
        this.nombreYApellido = nombreYApellido;
        this.contraseña = contraseña;
        this.dni = dni;
        this.mail = mail;
        this.edad = edad;
        historial = new ArrayList<>();
        facturas = new HashMap<>();
        this.estado = estado;
    }

    public Usuario() {
        nombreYApellido = "";
        contraseña = new Contraseña();
        dni = "";
        mail = new Mail();
        edad = 0;
        historial = new ArrayList<>();
        facturas = new HashMap<>();
        estado = false;
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

    public boolean encontrarPorMail(Mail mail) {
        boolean validacion = false;
        if (this.mail.equals(mail)) {
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
        return "👤 Usuario {\n" +
                "  👨‍💼 nombre y apellido = '" + nombreYApellido + "'\n" +
                "  🔒 contraseña = " + contraseña.toString() + "\n" +
                "  🆔 dni = '" + dni + "'\n" +
                "  ✉️ mail = " + mail.toString() + "\n" +
                "  🎂 edad = " + edad + "\n" +
                "  🌐 estado = " + estado + "\n" +
                "  📜 historial = " + historial + "\n" +
                "}";
    }


    /**
     * Comprueba que el usuario ingresado cumpla con los parametros para ser considerado un admin.
     * @param usuario
     * @return
     */
    public static boolean comprobarAdmin(Usuario usuario) {
        boolean validacion = false;
        if (usuario.getMail().getMail().equals("admin@gmail.com") && usuario.getContraseña().equals("Admin123")) {
            validacion = true;
        }
        return validacion;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("nombreYApellido", nombreYApellido);
        jsonObject.put("contraseña", contraseña.toJsonObj());
        jsonObject.put("dni", dni);
        jsonObject.put("mail", mail.toJsonObj());
        jsonObject.put("edad", edad);
        jsonObject.put("estado", estado);
        Iterator itF = facturas.entrySet().iterator();

        JSONArray array = new JSONArray();
        for (int i = 0; i < historial.size(); i++) {
            array.put(historial.get(i));
        }

        JSONArray jsonArrayF = new JSONArray();
        while (itF.hasNext()) {
            Map.Entry<Integer,Factura> factura = (Map.Entry<Integer, Factura>)itF.next();
            jsonArrayF.put(factura.getValue().toJsonObj());
        }

        jsonObject.put("historial", array);
        jsonObject.put("facturas", jsonArrayF);
        return jsonObject;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {
        setNombreYApellido(obj.getString("nombreYApellido"));
        Contraseña contra = new Contraseña();
        contra.fromJsonObj(obj.getJSONObject("contraseña"));
        setContraseña(contra);
        setDni(obj.getString("dni"));
        Mail mailAux = new Mail();
        mailAux.fromJsonObj(obj.getJSONObject("mail"));
        setMail(mailAux);
        setEdad(obj.getInt("edad"));
        setEstado(obj.getBoolean("estado"));
        JSONArray array = obj.getJSONArray("historial");

        for (int i = 0; i < array.length(); i++) {
            historial.add(array.getString(i));
        }
        JSONArray jsonArrayFacturas = obj.getJSONArray("facturas");

        for (int i = 0; i < jsonArrayFacturas.length(); i++) {
            Factura factura = new Factura();
            factura.fromJsonObj(jsonArrayFacturas.getJSONObject(i));
            facturas.put(factura.getId(), factura);
        }
    }

    public String getNombreYApellido() {
        return nombreYApellido;
    }

    public Contraseña getContraseña() {
        return contraseña;
    }


    private void setNombreYApellido(String nombreYApellido) {
        this.nombreYApellido = nombreYApellido;
    }

    private void setContraseña(Contraseña contraseña) {
        this.contraseña = contraseña;
    }

    private void setDni(String dni) {
        this.dni = dni;
    }

    private void setMail(Mail mail) {
        this.mail = mail;
    }

    private void setEdad(int edad) {
        this.edad = edad;
    }

    private void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getDni() {
        return dni;
    }

    public Mail getMail() {
        return mail;
    }

    public int getEdad() {
        return edad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void agregar(String dato) {
        historial.add(dato);
    }

    public void agregar(Factura factura) {
        facturas.put(factura.getId(), factura);
    }

    @Override
    public int compareTo(Object o) {
        int valor = 0;

        if (o != null) {
            if (o instanceof Usuario) {
                valor = dni.compareTo(((Usuario) o).getDni());
            }
        }
        return valor;
    }

    public String mostrarFacturas(){
        String listado = "";

        for(int i = 1; i<= facturas.size(); i++){
            listado = listado.concat(facturas.get(i).toString());
        }

        return listado;
    }
    
}
