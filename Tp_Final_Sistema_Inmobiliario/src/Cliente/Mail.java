package Cliente;

import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class Mail implements IJson {

    private String mail;

    public Mail(String mail) {
        this.mail = mail;
    }

    /**
     * Función que sirve para validar que sea un mail los el String que se pasa por parametros.
     * @param mail Es el mail a evaluar.
     * @return Retorna verdadero en caso de que sea un mail y false en caso de que no.
     */
    public static boolean validarMail(String mail) {
        boolean validacion = false;
        boolean arroba = false;
        boolean puntoCom = false;

        if (mail.contains("@")) {
            arroba = true;
        }
        if (mail.contains(".com")) {
            puntoCom = true;
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
            if (mail instanceof Mail) {
                if (Objects.equals(this.mail, ((Mail) mail).getMail())) {
                    validacion = true;
                }
            }
        }
        return validacion;
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
