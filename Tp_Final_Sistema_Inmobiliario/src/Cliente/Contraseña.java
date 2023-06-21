package Cliente;

import Excepciones.Contraseña.CantMayusException;
import Excepciones.Contraseña.CantNumException;
import Excepciones.Contraseña.TotalDigitosException;
import Interfaces.IJson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class Contraseña implements IJson, Comparable, Serializable {
    private String contraseña;

    public Contraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Contraseña() {
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
            throw new TotalDigitosException("Ingreso menos de 8 digitos a su contraseña, ingreso: ", contraseña.length());
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
                if (Objects.equals(this.contraseña, (String) contra)) {
                    validacion = true;
                }
            }
        }
        return validacion;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public int compareTo(Object o) {
        int valor = 0;
        if (o != null) {
            if (o instanceof Contraseña) {
                valor = contraseña.compareTo(((Contraseña) o).getContraseña());
            }
        }
        return valor;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("contraseña", contraseña);

        return jsonObject;
    }
    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {
        setContraseña(obj.getString("contraseña"));
    }

    private void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "🔐 Contraseña {\n" +
                "  🔑 Valor: '" + contraseña + "'\n" +
                "}";
    }
}
