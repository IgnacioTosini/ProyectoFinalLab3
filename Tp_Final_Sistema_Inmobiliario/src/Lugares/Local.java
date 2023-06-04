package Lugares;

public class Local {
    private String direccion;
    private Estado estado;
    private short ambientes;
    private boolean vidriera;
    private boolean vendido;
    private int precio;

    public Local(String direccion, Estado estado, short ambientes, boolean vidriera, boolean vendido, int precio) {
        this.direccion = direccion;
        this.estado = estado;
        this.ambientes = ambientes;
        this.vidriera = vidriera;
        this.vendido = vendido;
        this.precio = precio;
    }

    public Local() {
        direccion = "";
        estado = null;
        ambientes = 0;
        vidriera = false;
        vendido = false;
        precio = 0;
    }



    public String getDireccion() {
        return direccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public short getAmbientes() {
        return ambientes;
    }

    public boolean isVidriera() {
        return vidriera;
    }

    public boolean isVendido() {
        return vendido;
    }

    public int getPrecio() {
        return precio;
    }
}
