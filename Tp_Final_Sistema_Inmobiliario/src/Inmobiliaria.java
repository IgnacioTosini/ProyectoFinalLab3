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
    private TreeSet<Vivienda> viviendas; //(Ord por precio)
    private TreeSet<Cochera> cocheras; //(Ord por precio)
    private TreeSet<Local> locales;
    private HashMap<String, Usuario> usuarios;
    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;

    public Inmobiliaria(String nombre, String direccion, String telefono, String correo) {
        this.viviendas = new TreeSet<>();
        this.cocheras = new TreeSet<>();
        this.usuarios = new HashMap<>();
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
    }

    @Override
    public JSONObject toJsonObj() {
        return null;
    }


    @Override
    public void fromJsonObj(JSONObject obj) {

    }

    public Usuario buscarUsuario(String nombre) {

        return usuarios.get(nombre);
    }

    public void agregarUsuario(Usuario usuario) {

        usuarios.put(usuario.getNombreYApellido(), usuario);
    }


    public String listarViviendad(String eleccion, Estado estado) {
        String listado = "";

        if (viviendas != null) {
            if (eleccion.equalsIgnoreCase("casa")) {
                Iterator it = viviendas.iterator();
                while (it.hasNext()) {
                    if (it instanceof Casa) {
                        if (((Casa) it).getEstado().equals(estado)) {
                            listado.concat(it.toString() + '\n');
                        }
                    }
                    it.next();
                }
            } else if (eleccion.equalsIgnoreCase("departamento")) {
                Iterator it = viviendas.iterator();
                while (it.hasNext()) {
                    if (it instanceof Departamento) {
                        if (((Departamento) it).getEstado().equals(estado)) {
                            listado.concat(it.toString() + '\n');
                        }

                    }
                    it.next();
                }
            }

        }
        return listado;
    }

    public String listarLocales(Estado estado) {
        String listado = "";
        if (locales != null) {
            Iterator it = locales.iterator();
            while (it.hasNext()) {
                if (it instanceof Cochera) {
                    if (((Local) it).getEstado().equals(estado)) {
                        listado.concat(it.toString() + '\n');
                    }
                    it.next();
                }
            }
        }
        return listado;
    }


    public String listarCocheras(Estado estado) {
        String listado = "";
        if (cocheras != null) {
            Iterator it = cocheras.iterator();
            while (it.hasNext()) {

                if (it instanceof Cochera) {

                    if (((Cochera) it).getEstado().equals(estado)) {
                        listado.concat(it.toString() + '\n');
                    }

                }
                it.next();
            }

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
                        usuario.agregarHistorial(casa.getDireccion() + " " + fecha.toString());
                        casa.agregarDisponibilidad(fecha);
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
                        usuario.agregarHistorial(departamento.getDireccion() + " " + fecha.toString());
                        departamento.agregarDisponibilidad(fecha);


                        // agregar datos en el historial del usuario.
                        // agregar fecha nueva en el inmueble
                        // poner en lista de imprecion de factura
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
                        usuario.agregarHistorial(local.getDireccion() + " " + fecha.toString());
                        local.agregarDisponibilidad(fecha);
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
                        usuario.agregarHistorial(cochera.getDireccion() + " " + fecha.toString());
                        cochera.agregarDisponibilidad(fecha);
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
        boolean flag = false;
        if (viviendas != null) {
            Iterator it = viviendas.iterator();
            while (it.hasNext() && flag == false) {
                if (it instanceof Casa) {
                    if (((Casa) it).getDireccion().equals(direccion) && ((Casa) it).getEstado().name().equalsIgnoreCase("baja")) {
                        casa = (Casa) it;
                        flag = true;
                    }

                }
            }
            it.next();
        }
        return casa;
    }

    public Departamento buscarDepartamento(String direccion) {
        Departamento departamento = null;
        boolean flag = false;
        if (viviendas != null) {
            Iterator it = viviendas.iterator();
            while (it.hasNext() && flag == false) {
                if (it instanceof Departamento) {
                    if (((Departamento) it).getDireccion().equals(direccion)&& ((Departamento) it).getEstado().name().equalsIgnoreCase("baja")) {
                        departamento = (Departamento) it;
                        flag = true;
                    }

                }
            }
            it.next();
        }
        return departamento;
    }

    public Local buscarLocal(String direccion) {
        Local local = null;
        boolean flag = false;
        if (locales != null) {
            Iterator it = locales.iterator();
            while (it.hasNext() && flag == false) {
                if (it instanceof Local) {
                    if (((Local) it).getDireccion().equals(direccion) && ((Local) it).getEstado().name().equalsIgnoreCase("baja")) {
                        local = (Local) it;
                        flag = true;
                    }

                }
            }
            it.next();
        }
        return local;
    }

    public Cochera buscarCochera(String direccion) {
        Cochera cochera = null;
        boolean flag = false;
        if (cocheras != null) {
            Iterator it = cocheras.iterator();
            while (it.hasNext() && flag == false) {
                if (it instanceof Cochera) {
                    if (((Cochera) it).getDireccion().equals(direccion) && ((Cochera) it).getEstado().name().equalsIgnoreCase("baja")) {
                        cochera = (Cochera) it;
                        flag = true;
                    }

                }
            }
            it.next();
        }
        return cochera;
    }

}
