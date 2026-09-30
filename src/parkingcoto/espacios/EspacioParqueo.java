package parkingcoto.espacios;

import parkingcoto.vehiculos.Vehiculo;

public class EspacioParqueo {

    private final int numero;
    private final TipoEspacio tipo;
    private EstadoEspacio estado;

    public EspacioParqueo(int numero, TipoEspacio tipo) {
        this(numero, tipo, EstadoEspacio.DISPONIBLE);
    }

    public EspacioParqueo(int numero, TipoEspacio tipo, EstadoEspacio estadoInicial) {
        this.numero = numero;
        this.tipo = tipo;
        this.estado = estadoInicial;
    }

    public int getNumero() { return numero; }
    public TipoEspacio getTipo() { return tipo; }
    public EstadoEspacio getEstado() { return estado; }
}
