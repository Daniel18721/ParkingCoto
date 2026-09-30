package parkingcoto.vehiculos;

import parkingcoto.espacios.TipoEspacio;
import parkingcoto.tarifas.TarifaPorHoraConTope;

public class Automovil extends Vehiculo {

    public static final double TARIFA_POR_HORA = 900;
    public static final double TARIFA_MAXIMA_DIARIA = 7000;

    public Automovil(String placa, String marca, String modelo, String color) {
        super(placa, marca, modelo, color, TipoVehiculo.AUTOMOVIL,
              new TarifaPorHoraConTope(TARIFA_POR_HORA, TARIFA_MAXIMA_DIARIA));
    }

    @Override
    public TipoEspacio getTipoEspacioRequerido() {
        return TipoEspacio.AUTOMOVIL;
    }
}
