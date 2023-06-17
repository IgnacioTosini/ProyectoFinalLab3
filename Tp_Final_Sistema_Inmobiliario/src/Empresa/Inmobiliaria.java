package Empresa;

import Cliente.Usuario;
import Controladores.ControladoraInmobiliaria;
import Excepciones.EleccionIncorrectaException;
import Excepciones.LugarExistenteException;
import Excepciones.NoDisponibleException;
import Interfaces.IJson;
import Lugares.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class Inmobiliaria implements IJson {
    private Abm<Vivienda> viviendas; //(Ord por precio)
    private Abm<Cochera> cocheras; //(Ord por precio)
    private Abm<Local> locales;
    private HashMap<String, Usuario> usuarios;
    private HashMap<Integer,Factura> facturas;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;

    public Inmobiliaria(String nombre, String direccion, String telefono, String correo) {
        this.viviendas = new Abm<>();
        this.cocheras = new Abm<>();
        this.locales = new Abm<>();
        this.usuarios = new HashMap<>();
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        facturas = new HashMap<>();
    }

    @Override
    public JSONObject toJsonObj() {
        return null;
    }


    @Override
    public void fromJsonObj(JSONObject obj) {

    }

    public Usuario buscarUsuario(String mail) {
        return usuarios.get(mail);
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario != null) {
            usuarios.put(usuario.getNombreYApellido(), usuario);
        }
    }


    public String listarViviendad(String eleccion, Estado estado) {
        String listado = "";

        if (viviendas != null) {
            if (eleccion.equalsIgnoreCase("casa")) {
                listado = viviendas.listado("Casa");
            } else if (eleccion.equalsIgnoreCase("departamento")) {
                listado = viviendas.listado("Departamento");
            }
        }
        return listado;
    }

    public String listarLocales(Estado estado) {
        String listado = "";
        if (locales != null) {
            listado = locales.listado("Local");
        }
        return listado;
    }

    public String listarCocheras(Estado estado) {
        String listado = "";
        if (cocheras != null) {
            listado = cocheras.listado("Cochera");
        }
        return listado;
    }


    public void alquilar(Usuario usuario, String direccion, String tipo, Fecha fecha) throws NoDisponibleException, LugarExistenteException, EleccionIncorrectaException {  //Tipo es el tipo de inmueble al alquilar.
        double precioFinal = 0;
        switch (tipo.toLowerCase()) {
            case "casa":
                Casa casa = buscarCasa(direccion); //Excepcion en todos por si no se encuentra, si casa es null.
                if (casa != null) {
                    if (casa.validarFecha(fecha)) {
                        int eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        precioFinal = casa.metodoDePago(eleccion);
                        usuario.agregar(casa.getDireccion() + " " + fecha.toString());
                        casa.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(),precioFinal, fecha, direccion, casa.getDireccion(), estadoString(casa.getEstado()) );
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);

                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
                break;
            case "departamento":
                Departamento departamento = buscarDepartamento(direccion);

                if (departamento != null) {
                    if (departamento.validarFecha(fecha)) {
                        int eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        precioFinal = departamento.metodoDePago(eleccion);
                        usuario.agregar(departamento.getDireccion() + " " + fecha.toString());
                        departamento.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(),precioFinal, fecha, direccion, departamento.getDireccion(), estadoString(departamento.getEstado()) );
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }

                break;
            case "local":
                Local local = buscarLocal(direccion);
                if (local != null) {
                    if (local.validarFecha(fecha)) {
                        int eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        precioFinal = local.metodoDePago(eleccion);
                        usuario.agregar(local.getDireccion() + " " + fecha.toString());
                        local.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(),precioFinal, fecha, direccion, local.getDireccion(), estadoString(local.getEstado()) );
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
                break;
            case "cochera":
                Cochera cochera = buscarCochera(direccion);
                if (cochera != null) {
                    if (cochera.validarFecha(fecha)) {
                        int eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        precioFinal = cochera.metodoDePago(eleccion);
                        usuario.agregar(cochera.getDireccion() + " " + fecha.toString());
                        cochera.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(),precioFinal, fecha, direccion, cochera.getDireccion(), estadoString(cochera.getEstado()) );
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
                break;
            default:
                throw new EleccionIncorrectaException("Elección Invalida");

        }
    }

    public Casa buscarCasa(String direccion) {
        Casa casa = null;
        if (viviendas != null) {
            casa = (Casa) viviendas.buscador(direccion);
        }
        return casa;
    }

    public Departamento buscarDepartamento(String direccion) {
        Departamento departamento = null;
        if (viviendas != null) {
            departamento = (Departamento) viviendas.buscador(direccion);
        }
        return departamento;
    }

    public Local buscarLocal(String direccion) {
        Local local = null;
        if (locales != null) {
            local = locales.buscador(direccion);
        }
        return local;
    }

    public Cochera buscarCochera(String direccion) {
        Cochera cochera = null;
        if (cocheras != null) {
            cochera = cocheras.buscador(direccion);
        }
        return cochera;
    }

    public static String estadoString(Estado estado){
        String aux = estado.name();
        if(aux.equalsIgnoreCase("enalquiler")){
            aux = "alquilado";
        }

        return aux;
    }


}
