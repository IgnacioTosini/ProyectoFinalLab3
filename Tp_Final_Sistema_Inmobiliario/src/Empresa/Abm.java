package Empresa;

import Interfaces.IBuscar;
import Interfaces.IJson;
import Lugares.Casa;
import Lugares.Departamento;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class Abm<T extends IBuscar & IJson> {
    private HashSet<T> miHashSet;
    private HashSet<T> bajas;
    

    public Abm() {
        miHashSet = new HashSet<>();
        bajas = new HashSet<>();
    }

    public void agregar(T elemento) {
        if (bajas.contains(elemento)) {
            miHashSet.add(elemento);
            bajas.remove(elemento);
        } else {
            miHashSet.add(elemento);
        }
    }

    public boolean baja(T elemento) {
        boolean validacion = false;
        Iterator iterator = miHashSet.iterator();
        T aux = null;

        while (iterator.hasNext() && validacion == false) {
            aux =(T) iterator.next();
            if (elemento.equals(aux)) {
                bajas.add(elemento);
                miHashSet.remove(elemento);
                validacion = true;
            }
        }
        return validacion;
    }

    public void ponerEnBaja(T elemento){
        bajas.add(elemento);
    }

    public boolean modificar(T elemento) {
        boolean validacion = false;
        Iterator iterator = miHashSet.iterator();
        T aux = null;

        while (iterator.hasNext() && validacion == false) {
            aux = (T)iterator.next();
            if (elemento.equals(aux)) {
                miHashSet.remove(aux);
                miHashSet.add(elemento);
                validacion = true;
            }
        }
        return validacion;
    }

    public T buscador(String direccion) {
        boolean validacion = false;
        Iterator iterator = miHashSet.iterator();
        T buscado = null;

        while (iterator.hasNext() && validacion == false) {
            T aux = (T) iterator.next();
            if (aux.buscar(direccion)) {
                buscado = aux;
                validacion = true;
            }
        }

        return buscado;
    }

    public String listado(String nombreClase) {
        boolean validacion = false;
        Iterator iterator = miHashSet.iterator();
        String listado = "";
        String aux = "Lugares.";
        aux = aux.concat(nombreClase);
        
        T itAux = null;
        while (iterator.hasNext()) {
            itAux =(T) iterator.next();
            if (aux.equalsIgnoreCase(itAux.getClass().getName())) {
                listado = listado.concat(itAux.toString());
            }
        }
        return listado;
    }

    public int cantTotal() {
        return miHashSet.size();
    }

    public T get() {
        Iterator iterator = miHashSet.iterator();
        T objeto = null;

        while (iterator.hasNext()) {
            objeto = (T) iterator;
            iterator.next();
        }
        return objeto;
    }

    public JSONObject toJsonGenerico() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayCasa = new JSONArray();
        JSONArray jsonArrayDepartamento = new JSONArray();

        JSONArray jsonArrayBajasCasa = new JSONArray();
        JSONArray jsonArrayBajasDepartamento = new JSONArray();
        JSONArray jsonArrayBajas = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Iterator it = miHashSet.iterator();
        Iterator itBajas = bajas.iterator();

        while (it.hasNext()) {
            T aux = (T) it.next();

            if( aux instanceof Casa){
                jsonArrayCasa.put(aux.toJsonObj());
            }else if(aux instanceof Departamento){
                jsonArrayDepartamento.put(aux.toJsonObj());
            }else{
                jsonArray.put(aux.toJsonObj());
            }
        }

        while (itBajas.hasNext()) {
            T auxx = (T) itBajas.next();
            if( auxx instanceof Casa){
                jsonArrayBajasCasa.put(auxx.toJsonObj());
            }else if(auxx instanceof Departamento){
                jsonArrayBajasDepartamento.put(auxx.toJsonObj());
            }else{
                jsonArrayBajas.put(auxx.toJsonObj());
            }
        }

        jsonObject.put("casa", jsonArrayCasa);
        jsonObject.put("departamento", jsonArrayDepartamento);
        jsonObject.put("otros", jsonArray);
        jsonObject.put("casaBaja", jsonArrayBajasCasa);
        jsonObject.put("departamentoBajas", jsonArrayBajasDepartamento);
        jsonObject.put("otrosBajas", jsonArrayBajas);


        return jsonObject;
    }

    public void fromJsonGenerico(JSONObject obj) throws JSONException {
        JSONArray jsonArray = obj.getJSONArray("inmueble");
        JSONArray jsonArrayBajas = obj.getJSONArray("bajas");

        JSONObject aux = new JSONObject();
        T valor = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            aux = (JSONObject) jsonArray.get(i);
            valor.fromJsonObj(aux);

            miHashSet.add(valor);
        }

        for (int i = 0; i < jsonArrayBajas.length(); i++) {
            aux = (JSONObject) jsonArrayBajas.get(i);
            valor.fromJsonObj(aux);

            miHashSet.add(valor);
        }

    }
}