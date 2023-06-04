package Lugares;

/**
 * @author
 * @since
 * @see Departamento#isAmueblado()
 *
 */

public abstract class Vivienda {
    //private ArrayList<Fecha> disponibilidad;
    private Estado estado;
    private String direccion;
    private boolean vendido;
    private short ambientes;
    private short cantBanios;
    private int metrosCuadrados;
    private boolean amueblado;
    private boolean cochera;
    private int precio;

    public Vivienda(Estado estado, String direccion, boolean vendido , short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, int precio) {
        this.estado = estado;
        this.direccion = direccion;
        this.vendido = vendido;
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
        vendido = false;
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
                ", Precio: " + precio;
    }

    public Estado getEstado() {
        return estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public boolean isVendido() {
        return vendido;
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

    public int getPrecio() {
        return precio;
    }


}
