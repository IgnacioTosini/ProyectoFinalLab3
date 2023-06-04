package Lugares;

public class Departamento extends Vivienda{
    private String nroPiso;
    private String disposicion;

    public Departamento(Estado estado, String direccion, boolean vendido , short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, int precio, String nroPiso, String disposicion) {
        super(estado, direccion, vendido , ambientes, cantBanios, metrosCuadrados, amueblado, cochera, precio);
        this.nroPiso = nroPiso;
        this.disposicion = disposicion;
    }

    public Departamento() {
        super();
        nroPiso = "";
        disposicion = "";
    }

    @Override
    public String toString() {
        return super.toString() +
                "Tipo de Vivienda: Departamento" +
                "Numero de piso: '" + nroPiso + '\'' +
                ", Disposicion: '" + disposicion + '\'';
    }

    public String getNroPiso() {
        return nroPiso;
    }

    public String getDisposicion() {
        return disposicion;
    }
}
