package Cliente;

import Excepciones.ArrobaException;
import Excepciones.PuntoComException;
import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.WindowStateListener;
import java.util.Objects;

public class Mail implements IJson, Comparable {

    private String mail;

    public Mail(String mail) {
        this.mail = mail;
    }

    public Mail() {
        mail = "";
    }

    /**
     * Función que sirve para validar que sea un mail los el String que se pasa por parametros.
     *
     * @param mail Es el mail a evaluar.
     * @return Retorna verdadero en caso de que sea un mail y false en caso de que no.
     */
    public static boolean validarMail(String mail) throws ArrobaException, PuntoComException {
        boolean validacion = false;
        boolean arroba = false;
        boolean puntoCom = false;

        if (mail.contains("@")) {
            arroba = true;
        } else {
            throw new ArrobaException("Hace falta el @");
        }
        if (mail.contains(".com")) {
            puntoCom = true;
        } else {
            throw new PuntoComException("Hace falta .com al final");
        }
        if (arroba == true && puntoCom == true) {
            validacion = true;
        }
        return validacion;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public boolean equals(Object mail) {
        boolean validacion = false;

        if (mail != null) {
            if (mail instanceof String) {
                if (this.mail.equalsIgnoreCase((String) mail)) {
                    validacion = true;
                }
            }
        }
        return validacion;
    }

    @Override
    public int hashCode(){
        return 1;
    }

    @Override
    public int compareTo(Object o) {
        int valor = 0;
        if(o != null){
            if(o instanceof Mail){
                valor = mail.compareTo(((Mail) o).getMail());
            }
        }
        return valor;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("mail", mail);

        return jsonObject;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {
        mail = obj.getString("mail");
    }

    @Override
    public String toString() {
        return "Mail{" +
                "mail='" + mail + '\'' +
                '}';
    }
}
