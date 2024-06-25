package pe.edu.upc.TFFinanzas.entities;

public enum NumeroDiasCuotaEnum {
    CERO(0),
    QUINCE(15),
    TREINTA(30);

    private final int dias;

    NumeroDiasCuotaEnum(int dias) {
        this.dias = dias;
    }

    public int getDias() {
        return dias;
    }
}
