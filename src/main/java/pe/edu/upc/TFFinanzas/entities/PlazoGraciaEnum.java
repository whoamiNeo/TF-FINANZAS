package pe.edu.upc.TFFinanzas.entities;

public enum PlazoGraciaEnum {
    CERO(0),
    UNO(1),
    DOS(2);

    private final int periodos;

    PlazoGraciaEnum(int periodos) {
        this.periodos = periodos;
    }

    public int getCuotas() {
        return periodos;
    }
}
