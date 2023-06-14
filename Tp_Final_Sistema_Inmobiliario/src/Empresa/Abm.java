package Empresa;

import Interfaces.IBuscar;

import java.util.Iterator;
import java.util.TreeSet;

public class Abm<T extends IBuscar> {

    private TreeSet<T> miTreeSet;
    private TreeSet<T> bajas;

    public Abm() {
        miTreeSet = new TreeSet<>();
        bajas = new TreeSet<>();
    }

    public void agregar(T elemento) {
        if (bajas.contains(elemento)) {
            miTreeSet.add(elemento);
            bajas.remove(elemento);
        } else {
            miTreeSet.add(elemento);
        }
    }

    public boolean baja(T elemento) {
        boolean validacion = false;
        Iterator iterator = miTreeSet.iterator();

        while (iterator.hasNext() && validacion == false) {
            if (elemento.equals((T) iterator)) {
                bajas.add(elemento);
                miTreeSet.remove(elemento);
                validacion = true;
            }
            iterator.next();
        }
        return validacion;
    }

    public boolean modificar(T elemento) {
        boolean validacion = false;
        Iterator iterator = miTreeSet.iterator();

        while (iterator.hasNext() && validacion == false) {
            if (elemento.equals((T) iterator)) {
                miTreeSet.remove((T) iterator);
                miTreeSet.add(elemento);
                validacion = true;
            }
            iterator.next();
        }
        return validacion;
    }

    public T buscador(String direccion) {
        boolean validacion = false;
        Iterator iterator = miTreeSet.iterator();
        T buscado = null;

        while (iterator.hasNext() && validacion == false) {
            if (((T) iterator).buscar(direccion)) {
                buscado = (T) iterator;
                validacion = true;
            }
            iterator.next();
        }
        return buscado;
    }

    public String listado(String nombreClase) {
        boolean validacion = false;
        Iterator iterator = miTreeSet.iterator();
        String listado = "";

        while (iterator.hasNext()) {
            if (nombreClase.equalsIgnoreCase(iterator.getClass().getName())){
                listado.concat(iterator.toString());
            }
            iterator.next();
        }
        return listado;
    }
}