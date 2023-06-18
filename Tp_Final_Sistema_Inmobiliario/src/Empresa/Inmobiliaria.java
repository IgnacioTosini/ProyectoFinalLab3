package Empresa;

import Cliente.Usuario;
import Controladores.ControladoraInmobiliaria;
import Excepciones.EleccionIncorrectaException;
import Excepciones.LugarExistenteException;
import Excepciones.NoDisponibleException;
import Interfaces.IJson;
import Lugares.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

public class Inmobiliaria implements IJson {
    private Abm<Vivienda> viviendas; //(Ord por precio)
    private Abm<Cochera> cocheras; //(Ord por precio)
    private Abm<Local> locales;
    private HashMap<String, Usuario> usuarios;
    private HashMap<Integer, Factura> facturas;
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

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    private void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public JSONObject toJsonObj() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArrayF = new JSONArray();
        JSONArray jsonArrayU = new JSONArray();
        Iterator itF = facturas.entrySet().iterator();
        Iterator itU = usuarios.entrySet().iterator();

        jsonObject.put("nombre", nombre);
        jsonObject.put("direccion", direccion);
        jsonObject.put("telefono", telefono);
        jsonObject.put("correo", correo);
        jsonObject.put("viviendas", viviendas.toJsonGenerico());
        jsonObject.put("cocheras", cocheras.toJsonGenerico());
        jsonObject.put("locales", locales.toJsonGenerico());

        while (itF.hasNext()) {
            jsonArrayF.put(itF);
            itF.next();
        }

        while (itU.hasNext()) {
            jsonArrayU.put(itU);
            itU.next();
        }

        jsonObject.put("facturas", jsonArrayF);
        jsonObject.put("usuarios", jsonArrayU);

        return jsonObject;
    }

    @Override
    public void fromJsonObj(JSONObject obj) throws JSONException {
        setNombre(obj.getString("nombre"));
        setCorreo(obj.getString("correo"));
        setDireccion(obj.getString("direccion"));
        setTelefono(obj.getString("telefono"));

        JSONObject jsonArrayViviendas = obj.getJSONObject("viviendas");
        JSONObject jsonArrayCocheras = obj.getJSONObject("cocheras");
        JSONObject jsonArrayLocales = obj.getJSONObject("locales");
        JSONArray jsonArrayFacturas = obj.getJSONArray("facturas");
        JSONArray jsonArrayUsuarios = obj.getJSONArray("usuarios");

        viviendas.fromJsonGenerico(jsonArrayViviendas);
        cocheras.fromJsonGenerico(jsonArrayCocheras);
        locales.fromJsonGenerico(jsonArrayLocales);

        for (int i = 0; i < jsonArrayFacturas.length(); i++) {
            Factura factura = new Factura();
            factura.fromJsonObj(jsonArrayFacturas.getJSONObject(i));
            facturas.put(factura.getId(), factura);
        }

        for (int i = 0; i < jsonArrayUsuarios.length(); i++) {
            Usuario usuario = new Usuario();
            usuario.fromJsonObj(jsonArrayUsuarios.getJSONObject(i));
            usuarios.put(usuario.getMail().getMail(), usuario);
        }
    }

    public Usuario buscarUsuario(String mail) {

        return usuarios.get(mail); // ver
    }

    public void agregarUsuario(Usuario usuario) {
        if (usuario != null) {
            usuarios.put(usuario.getMail().getMail(), usuario);
        }
    }

    public boolean darBaja(String mail) {
        boolean validacion = false;
        Usuario aux = buscarUsuario(mail);
        Usuario usuario = new Usuario(aux.getNombreYApellido(), aux.getContraseña(), aux.getDni(), aux.getMail(), aux.getEdad(), false);
        if (usuario != null) {
            usuarios.remove(aux);
            agregarUsuario(usuario);
            validacion = true;
        }
        return validacion;
    }

    public String listarViviendad(String eleccion) {
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

    public String listarLocales() {
        String listado = "";
        if (locales != null) {
            listado = locales.listado("Local");
        }
        return listado;
    }

    public String listarCocheras() {
        String listado = "";
        if (cocheras != null) {
            listado = cocheras.listado("Cochera");
        }
        return listado;
    }

    public void alquilar(Usuario usuario, String direccion, String tipo, Fecha fecha) throws NoDisponibleException, LugarExistenteException {  //Tipo es el tipo de inmueble al alquilar.
        double precioFinal = 0;
        switch (tipo.toLowerCase()) {
            case "casa" -> {
                Casa casa = buscarCasa(direccion); //Excepcion en todos por si no se encuentra, si casa es null.
                if (casa != null) {
                    if (casa.validarFecha(fecha)) {
                        int eleccion = 0;
                        try {
                            eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        try {
                            precioFinal = casa.metodoDePago(eleccion);
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        usuario.agregar(casa.getDireccion() + " " + fecha.toString());
                        casa.agregarDisponibilidad(fecha);
                        Factura factura = null;
                        if (facturas.size() == 0) {
                            factura = new Factura(1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(), precioFinal, fecha, direccion, casa.getDireccion(), estadoString(casa.getEstado()));
                        }else{
                            factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(), precioFinal, fecha, direccion, casa.getDireccion(), estadoString(casa.getEstado()));
                        }
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
            }
            case "departamento" -> {
                Departamento departamento = buscarDepartamento(direccion);
                if (departamento != null) {
                    if (departamento.validarFecha(fecha)) {
                        int eleccion = 0;
                        try {
                            eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        try {
                            precioFinal = departamento.metodoDePago(eleccion);
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        usuario.agregar(departamento.getDireccion() + " " + fecha.toString());
                        departamento.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(), precioFinal, fecha, direccion, departamento.getDireccion(), estadoString(departamento.getEstado()));
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
            }
            case "local" -> {
                Local local = buscarLocal(direccion);
                if (local != null) {
                    if (local.validarFecha(fecha)) {
                        int eleccion = 0;
                        try {
                            eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        try {
                            precioFinal = local.metodoDePago(eleccion);
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());

                        }
                        usuario.agregar(local.getDireccion() + " " + fecha.toString());
                        local.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(), precioFinal, fecha, direccion, local.getDireccion(), estadoString(local.getEstado()));
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
            }
            case "cochera" -> {
                Cochera cochera = buscarCochera(direccion);
                if (cochera != null) {
                    if (cochera.validarFecha(fecha)) {
                        int eleccion = 0;
                        try {
                            eleccion = ControladoraInmobiliaria.eleccionMetodoDePago();
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        try {
                            precioFinal = cochera.metodoDePago(eleccion);
                        } catch (EleccionIncorrectaException e) {
                            System.err.println(e.getMessage());
                        }
                        usuario.agregar(cochera.getDireccion() + " " + fecha.toString());
                        cochera.agregarDisponibilidad(fecha);
                        Factura factura = new Factura(facturas.size() + 1, usuario.getNombreYApellido(), usuario.getDni(), usuario.getMail(), precioFinal, fecha, direccion, cochera.getDireccion(), estadoString(cochera.getEstado()));
                        facturas.put(factura.getId(), factura);
                        usuario.agregar(factura);
                    } else {
                        throw new NoDisponibleException("Esa fecha no se encuentra disponible"); //agregar a la exception una lista de fechas demandadas
                    }
                } else {
                    throw new LugarExistenteException("La dirección ingresada no existe");
                }
            }
        }
    }

    public Casa buscarCasa(String direccion) {
        Casa casa = null;
        if (viviendas != null) {
            casa = (Casa) viviendas.buscador(direccion);
            System.out.println(casa.toString());
        }
        return casa;
    }

    public Departamento buscarDepartamento(String direccion) {
        Departamento departamento = null;
        if (viviendas != null) {
            departamento = (Departamento) viviendas.buscador(direccion);
            System.out.println(departamento.toString());
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

    public static String estadoString(Estado estado) {
        String aux = estado.name();
        if (aux.equalsIgnoreCase("enalquiler")) {
            aux = "alquilado";
        }

        return aux;
    }

    public void agregar(Vivienda vivienda) {
        viviendas.agregar(vivienda);
    }

    public void agregar(Local local) {
        locales.agregar(local);
    }

    public void agregar(Cochera cochera) {
        cocheras.agregar(cochera);
    }

    public boolean baja(Vivienda vivienda) {
        return viviendas.baja(vivienda);
    }

    public boolean baja(Local local) {
        return locales.baja(local);
    }

    public boolean baja(Cochera cochera) {
        return cocheras.baja(cochera);
    }

    public boolean modificar(Vivienda vivienda) {
        return viviendas.modificar(vivienda);
    }

    public boolean modificar(Local local) {
        return locales.modificar(local);
    }

    public boolean modificar(Cochera cochera) {
        return cocheras.modificar(cochera);
    }

}
