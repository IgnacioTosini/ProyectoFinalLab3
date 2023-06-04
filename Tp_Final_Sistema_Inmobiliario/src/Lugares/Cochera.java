package Lugares;

public class Cochera {
    private String direccion;
    private Estado estado;
    private short piso;
    private short posicion;
    private boolean vendido;
    private String medioDeAcceso;
    private boolean ascensor;

    public Cochera(String direccion, Estado estado, short piso, short posicion, boolean vendido, String medioDeAcceso, boolean ascensor) {
        this.direccion = direccion;
        this.estado = estado;
        this.piso = piso;
        this.posicion = posicion;
        this.vendido = vendido;
        this.medioDeAcceso = medioDeAcceso;
        this.ascensor = ascensor;
    }

    public Cochera() {
        direccion = "";
        estado = null;
        piso = 0;
        posicion = 0;
        vendido = false;
        medioDeAcceso = "";
        ascensor = false;
    }



    public String getDireccion() {
        return direccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public short getPiso() {
        return piso;
    }

    public short getPosicion() {
        return posicion;
    }

    public boolean isVendido() {
        return vendido;
    }

    public String getMedioDeAcceso() {
        return medioDeAcceso;
    }

    public boolean isAscensor() {
        return ascensor;
    }
}
