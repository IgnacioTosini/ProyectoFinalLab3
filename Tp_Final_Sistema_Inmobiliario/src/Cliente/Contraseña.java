package Cliente;

import Excepciones.CantMayusException;
import Excepciones.CantNumException;
import Excepciones.TotalDigitosException;
import Interfaces.IJson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Contraseña implements IJson {

    private String contraseña;

    public Contraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Contraseña(){
        contraseña = "";
    }

    /**
     * Sistema de control de requisitos minimos para crear contrasñas (1 mayuscula, 1 numero y 8 carcateres).
     *
     * @param contraseña, es la contraseña a evaluar.
     * @throws CantMayusException    Exception cuando la cantidad de mayusculas es menor a 1.
     * @throws CantNumException      Exception cuando la cantidad de numeros es menor a 1.
     * @throws TotalDigitosException Excepciòn cuando la contraseña tiene menos de 8 caracteres.
     */
    public static void verificacion(String contraseña) throws CantMayusException, CantNumException, TotalDigitosException {
        int cantNum = 0;
        int cantMayus = 0;

        if (contraseña.length() < 8) {
            throw new TotalDigitosException("Ingreso menos de 8 digitos a su contraseña", contraseña.length());
        }

        for (int i = 0; i < contraseña.length(); i++) {
            if (Character.isDigit(contraseña.charAt(i))) {
                cantNum++;
            }
            if (Character.isUpperCase(contraseña.charAt(i))) {
                cantMayus++;
            }
        }

        if (cantMayus < 1) {
            throw new CantMayusException("No ingreso la mayuscula en su contraseña");
        }
        if (cantNum < 1) {
            throw new CantNumException("No ingreso numeros a su contraseña");
        }
    }

    public String getContraseña() {
        return contraseña;
    }

    @Override
    public boolean equals(Object contra) {
        boolean validacion = false;

        if (contra != null) {
            if (contra instanceof String) {
                if (Objects.equals(this.contraseña, (String) contra )) {
                    validacion = true;
                }
            }
        }
        return validacion;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();

            jsonObject.put("contraseña", contraseña);


        return jsonObject;
    }


    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {


            contraseña = obj.getString("contraseña");


    }
}
