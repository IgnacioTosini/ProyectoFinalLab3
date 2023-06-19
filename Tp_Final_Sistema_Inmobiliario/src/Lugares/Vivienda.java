package Lugares;

import Interfaces.*;

import java.util.ArrayList;

/**
 * @author
 * @see Departamento#isAmueblado()
 * @since
 */

public abstract class Vivienda implements IComprobarFecha, IMetodoDePago, Comparable, IBuscar, IJson, IBaja{
    private ArrayList<Fecha> disponibilidad;
    private Estado estado;
    private String direccion;
    //La disponibilidad se hara en un metodo donde va a evaluar la fecha del alquiler
    private short ambientes;
    private short cantBanios;
    private int metrosCuadrados;
    private boolean amueblado;
    private boolean cochera;
    private double precio;

    public Vivienda(Estado estado, String direccion, short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, double precio) {
        this.estado = estado;
        this.direccion = direccion;
        disponibilidad = new ArrayList<>();
        this.ambientes = ambientes;
        this.cantBanios = cantBanios;
        this.metrosCuadrados = metrosCuadrados;
        this.amueblado = amueblado;
        this.cochera = cochera;
        this.precio = precio;
    }

    public Vivienda() {
        estado = null;
        direccion = "";
        disponibilidad = new ArrayList<>();
        ambientes = 0;
        cantBanios = 0;
        metrosCuadrados = 0;
        amueblado = false;
        cochera = false;
        precio = 0;
    }

    @Override
    public String toString() {
        return "Vivienda: " + '\n' +
                "Tipo de venta: " + estado +
                ", Direccion: '" + direccion + '\'' +
                ", Ambientes: " + ambientes +
                ", Cantidad de Baños: " + cantBanios +
                ", Metros Cuadrados: " + metrosCuadrados +
                ", Amueblado: " + amueblado +
                ", Cochera: " + cochera +
                ", Precio: " + precio +
                ", Fechas: " + mostrarFechas();
    }

    public Estado getEstado() {
        return estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public short getAmbientes() {
        return ambientes;
    }

    public short getCantBanios() {
        return cantBanios;
    }

    public int getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public boolean isAmueblado() {
        return amueblado;
    }

    public boolean isCochera() {
        return cochera;
    }

    public double getPrecio() {
        return precio;
    }

    public void agregarDisponibilidad(Fecha fecha) {
        disponibilidad.add(fecha);
    }

    public Fecha buscarFecha(int pos) {
        return disponibilidad.get(pos);
    }

    public int cantDeFechas() {
        return disponibilidad.size();
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setAmbientes(short ambientes) {
        this.ambientes = ambientes;
    }

    public void setCantBanios(short cantBanios) {
        this.cantBanios = cantBanios;
    }

    public void setMetrosCuadrados(int metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public void setAmueblado(boolean amueblado) {
        this.amueblado = amueblado;
    }

    public void setCochera(boolean cochera) {
        this.cochera = cochera;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    @Override
    public boolean validarFecha(Fecha fecha) {
        Fecha aux = new Fecha();
        boolean validacion = false;
        if (disponibilidad.size() == 0) {
            validacion = true;
        } else {
            for (int i = 0; i < disponibilidad.size(); i++) {
                aux = disponibilidad.get(i);
                if (aux.comprobarFecha(fecha)) {
                    validacion = true;
                }
            }
        }

        return validacion;
    }

    @Override
    public boolean buscar(String direccion) {
        boolean encontrado = false;
        if (this.direccion.equalsIgnoreCase(direccion)) {
            encontrado = true;
        }
        return encontrado;
    }

    public String mostrarFechas(){
        String listado = "";
        for(Fecha fecha: disponibilidad){
            listado = listado.concat(fecha.toString()) + '\n';
        }
        return listado;
    }



    public void baja(){
        estado = Estado.Baja;
    }
}
