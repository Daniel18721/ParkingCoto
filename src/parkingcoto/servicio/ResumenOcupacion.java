package parkingcoto.servicio;

import parkingcoto.espacios.TipoEspacio;

public class ResumenOcupacion {

    private final TipoEspacio tipo;
    private final int total;
    private final int ocupados;
    private final int disponibles;
    private final int fueraDeServicio;

    public ResumenOcupacion(TipoEspacio tipo, int total, int ocupados,
                            int disponibles, int fueraDeServicio) {
        this.tipo = tipo;
        this.total = total;
        this.ocupados = ocupados;
        this.disponibles = disponibles;
        this.fueraDeServicio = fueraDeServicio;
    }

    public TipoEspacio getTipo() { return tipo; }
    public int getTotal() { return total; }
    public int getOcupados() { return ocupados; }
    public int getDisponibles() { return disponibles; }
    public int getFueraDeServicio() { return fueraDeServicio; }
}
