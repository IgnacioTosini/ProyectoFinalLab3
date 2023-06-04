package Lugares;

public class Casa extends Vivienda {
    private boolean patio;
    private short pisos;

    public Casa(Estado estado, String direccion, boolean vendido, short ambientes, short cantBanios, int metrosCuadrados, boolean amueblado, boolean cochera, int precio, boolean patio, short pisos) {
        super(estado, direccion, vendido , ambientes, cantBanios, metrosCuadrados, amueblado, cochera, precio);
        this.patio = patio;
        this.pisos = pisos;
    }

    public Casa() {
        super();
        patio = false;
        pisos = 0;
    }

    @Override
    public String toString() {
        return super.toString() +
                "Tipo Vivienda: Casa" +
                "Patio: " + patio +
                ", Pisos: " + pisos;
    }

    public boolean isPatio() {
        return patio;
    }

    public short getPisos() {
        return pisos;
    }
}
